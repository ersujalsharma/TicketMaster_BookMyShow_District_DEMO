package com.sujal.ticketmaster.service;

import com.sujal.ticketmaster.dto.EventDTO;
import com.sujal.ticketmaster.dto.ShowDTO;
import com.sujal.ticketmaster.dto.ShowSeatDTO;
import com.sujal.ticketmaster.entity.*;
import com.sujal.ticketmaster.exception.ResourceNotFoundException;
import com.sujal.ticketmaster.repository.EventRepository;
import com.sujal.ticketmaster.repository.ShowRepository;
import com.sujal.ticketmaster.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toEventDTO)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return toEventDTO(event);
    }

    public List<EventDTO> searchEvents(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toEventDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByType(EventType type) {
        return eventRepository.findByType(type).stream()
                .map(this::toEventDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByLocation(Long locationId) {
        return eventRepository.findEventsByLocationId(locationId).stream()
                .map(this::toEventDTO)
                .collect(Collectors.toList());
    }

    public EventDTO createEvent(EventDTO dto) {
        Event event = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(dto.getType())
                .language(dto.getLanguage())
                .posterUrl(dto.getPosterUrl())
                .build();
        Event saved = eventRepository.save(event);
        return toEventDTO(saved);
    }

    // --- Show related ---

    public List<ShowDTO> getShowsForEvent(Long eventId) {
        return showRepository.findByEventId(eventId).stream()
                .map(this::toShowDTO)
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsForVenue(Long venueId) {
        return showRepository.findByVenueId(venueId).stream()
                .map(this::toShowDTO)
                .collect(Collectors.toList());
    }

    public ShowDTO getShowById(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + showId));
        return toShowDTO(show);
    }

    public List<ShowSeatDTO> getAvailableSeats(Long showId) {
        return showSeatRepository.findByShowIdAndStatus(showId, SeatStatus.AVAILABLE).stream()
                .map(this::toShowSeatDTO)
                .collect(Collectors.toList());
    }

    public List<ShowSeatDTO> getAllSeatsForShow(Long showId) {
        return showSeatRepository.findByShowId(showId).stream()
                .map(this::toShowSeatDTO)
                .collect(Collectors.toList());
    }

    private EventDTO toEventDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .type(event.getType())
                .language(event.getLanguage())
                .posterUrl(event.getPosterUrl())
                .build();
    }

    private ShowDTO toShowDTO(Show show) {
        return ShowDTO.builder()
                .id(show.getId())
                .date(show.getDate())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .eventId(show.getEvent().getId())
                .eventName(show.getEvent().getName())
                .venueId(show.getVenue().getId())
                .venueName(show.getVenue().getName())
                .build();
    }

    private ShowSeatDTO toShowSeatDTO(ShowSeat showSeat) {
        return ShowSeatDTO.builder()
                .id(showSeat.getId())
                .seatNumber(showSeat.getSeat().getSeatNumber())
                .seatRow(showSeat.getSeat().getSeatRow())
                .seatType(showSeat.getSeat().getSeatType())
                .price(showSeat.getPrice())
                .status(showSeat.getStatus())
                .build();
    }
}
