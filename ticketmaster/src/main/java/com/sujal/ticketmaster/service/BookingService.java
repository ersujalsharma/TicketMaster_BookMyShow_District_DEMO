package com.sujal.ticketmaster.service;

import com.sujal.ticketmaster.dto.BookingRequest;
import com.sujal.ticketmaster.dto.BookingResponse;
import com.sujal.ticketmaster.dto.ShowSeatDTO;
import com.sujal.ticketmaster.entity.*;
import com.sujal.ticketmaster.exception.ResourceNotFoundException;
import com.sujal.ticketmaster.exception.SeatAlreadyBookedException;
import com.sujal.ticketmaster.exception.SeatLockException;
import com.sujal.ticketmaster.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;

    private static final Duration SEAT_LOCK_TTL = Duration.ofMinutes(10);
    private static final String SEAT_LOCK_PREFIX = "seat_lock:";

    /**
     * Books seats for a user with concurrent safety.
     * 
     * Flow:
     * 1. Lock seats in Redis (temporary hold)
     * 2. Acquire pessimistic DB lock on the ShowSeat rows
     * 3. Verify seats are still AVAILABLE
     * 4. Mark seats as LOCKED (waiting for payment)
     * 5. Create booking in PENDING status
     */
    @Transactional
    public BookingResponse bookSeats(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + request.getShowId()));

        // Step 1: Try to acquire Redis locks for each seat
        List<String> lockedKeys = acquireRedisLocks(request.getShowSeatIds(), request.getUserId());

        try {
            // Step 2: Acquire pessimistic locks on DB rows
            List<ShowSeat> showSeats = showSeatRepository.findAllByIdWithLock(request.getShowSeatIds());

            if (showSeats.size() != request.getShowSeatIds().size()) {
                throw new ResourceNotFoundException("One or more seats not found");
            }

            // Step 3: Verify all seats are available
            for (ShowSeat seat : showSeats) {
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    throw new SeatAlreadyBookedException(
                            "Seat " + seat.getSeat().getSeatNumber() + " is already " + seat.getStatus());
                }
            }

            // Step 4: Mark seats as LOCKED
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (ShowSeat seat : showSeats) {
                seat.setStatus(SeatStatus.LOCKED);
                totalAmount = totalAmount.add(seat.getPrice());
            }
            showSeatRepository.saveAll(showSeats);

            // Step 5: Create booking
            Booking booking = Booking.builder()
                    .user(user)
                    .show(show)
                    .status(BookingStatus.PENDING)
                    .totalAmount(totalAmount)
                    .seats(showSeats)
                    .build();
            Booking savedBooking = bookingRepository.save(booking);

            log.info("Booking created: {} for user: {} with {} seats",
                    savedBooking.getId(), user.getId(), showSeats.size());

            return toBookingResponse(savedBooking, showSeats);

        } catch (Exception e) {
            // Release Redis locks on failure
            releaseRedisLocks(lockedKeys);
            throw e;
        }
    }

    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return toBookingResponse(booking, booking.getSeats());
    }

    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(b -> toBookingResponse(b, b.getSeats()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel a confirmed booking. Request refund instead.");
        }

        // Release seats
        for (ShowSeat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
            releaseRedisLock(seat.getId());
        }
        showSeatRepository.saveAll(booking.getSeats());

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Booking {} cancelled", bookingId);
    }

    // --- Redis Lock Methods ---

    private List<String> acquireRedisLocks(List<Long> seatIds, Long userId) {
        List<String> keys = seatIds.stream()
                .map(id -> SEAT_LOCK_PREFIX + id)
                .collect(Collectors.toList());

        for (String key : keys) {
            Boolean acquired = redisTemplate.opsForValue()
                    .setIfAbsent(key, userId.toString(), SEAT_LOCK_TTL);

            if (acquired == null || !acquired) {
                // Release any locks we already acquired
                releaseRedisLocks(keys);
                throw new SeatLockException("Seat is currently being held by another user. Please try again.");
            }
        }
        return keys;
    }

    private void releaseRedisLocks(List<String> keys) {
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    private void releaseRedisLock(Long seatId) {
        redisTemplate.delete(SEAT_LOCK_PREFIX + seatId);
    }

    private BookingResponse toBookingResponse(Booking booking, List<ShowSeat> showSeats) {
        List<ShowSeatDTO> seatDTOs = showSeats.stream()
                .map(ss -> ShowSeatDTO.builder()
                        .id(ss.getId())
                        .seatNumber(ss.getSeat().getSeatNumber())
                        .seatRow(ss.getSeat().getSeatRow())
                        .seatType(ss.getSeat().getSeatType())
                        .price(ss.getPrice())
                        .status(ss.getStatus())
                        .build())
                .collect(Collectors.toList());

        String ticketNumber = booking.getTicket() != null ? booking.getTicket().getTicketNumber() : null;

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .userId(booking.getUser().getId())
                .showId(booking.getShow().getId())
                .eventName(booking.getShow().getEvent().getName())
                .venueName(booking.getShow().getVenue().getName())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .seats(seatDTOs)
                .bookedAt(booking.getBookedAt())
                .ticketNumber(ticketNumber)
                .build();
    }
}
