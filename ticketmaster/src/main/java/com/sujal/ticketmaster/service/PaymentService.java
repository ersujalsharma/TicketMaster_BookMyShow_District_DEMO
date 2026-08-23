package com.sujal.ticketmaster.service;

import com.sujal.ticketmaster.dto.PaymentRequest;
import com.sujal.ticketmaster.dto.PaymentResponse;
import com.sujal.ticketmaster.entity.*;
import com.sujal.ticketmaster.exception.ResourceNotFoundException;
import com.sujal.ticketmaster.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ShowSeatRepository showSeatRepository;
    private final TicketRepository ticketRepository;

    /**
     * Process payment for a booking.
     * 
     * Flow:
     * 1. Validate booking exists and is in PENDING status
     * 2. Simulate payment processing
     * 3. Update booking status to CONFIRMED
     * 4. Update seat status from LOCKED to BOOKED
     * 5. Generate ticket
     */
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING status. Current status: " + booking.getStatus());
        }

        // Create payment record
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .transactionId(generateTransactionId())
                .build();
        payment = paymentRepository.save(payment);

        // Simulate payment processing (in real system, call payment gateway)
        boolean paymentSuccess = simulatePaymentProcessing(payment);

        if (paymentSuccess) {
            // Update payment
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Update booking status
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            // Update seats from LOCKED to BOOKED
            for (ShowSeat seat : booking.getSeats()) {
                seat.setStatus(SeatStatus.BOOKED);
            }
            showSeatRepository.saveAll(booking.getSeats());

            // Generate ticket
            Ticket ticket = generateTicket(booking);

            log.info("Payment successful for booking {}. Ticket: {}", booking.getId(), ticket.getTicketNumber());

            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .bookingId(booking.getId())
                    .amount(payment.getAmount())
                    .status(payment.getStatus())
                    .paymentMethod(payment.getPaymentMethod())
                    .transactionId(payment.getTransactionId())
                    .paidAt(payment.getPaidAt())
                    .ticketNumber(ticket.getTicketNumber())
                    .build();
        } else {
            // Payment failed
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            // Release seats back to AVAILABLE
            for (ShowSeat seat : booking.getSeats()) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
            showSeatRepository.saveAll(booking.getSeats());

            // Mark booking as CANCELLED
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            log.warn("Payment failed for booking {}", booking.getId());

            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .bookingId(booking.getId())
                    .amount(payment.getAmount())
                    .status(payment.getStatus())
                    .paymentMethod(payment.getPaymentMethod())
                    .transactionId(payment.getTransactionId())
                    .build();
        }
    }

    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));

        String ticketNumber = null;
        Ticket ticket = ticketRepository.findByBookingId(bookingId).orElse(null);
        if (ticket != null) {
            ticketNumber = ticket.getTicketNumber();
        }

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .paidAt(payment.getPaidAt())
                .ticketNumber(ticketNumber)
                .build();
    }

    private Ticket generateTicket(Booking booking) {
        String ticketNumber = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNumber)
                .booking(booking)
                .build();
        return ticketRepository.save(ticket);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private boolean simulatePaymentProcessing(Payment payment) {
        // In production, integrate with Razorpay/Stripe/PayU
        // For now, always return success
        return true;
    }
}
