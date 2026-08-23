package com.sujal.ticketmaster.controller;

import com.sujal.ticketmaster.dto.EventDTO;
import com.sujal.ticketmaster.dto.ShowDTO;
import com.sujal.ticketmaster.dto.ShowSeatDTO;
import com.sujal.ticketmaster.entity.EventType;
import com.sujal.ticketmaster.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String name) {
        return ResponseEntity.ok(eventService.searchEvents(name));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EventDTO>> getEventsByType(@PathVariable EventType type) {
        return ResponseEntity.ok(eventService.getEventsByType(type));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<EventDTO>> getEventsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(eventService.getEventsByLocation(locationId));
    }

    @GetMapping("/{eventId}/shows")
    public ResponseEntity<List<ShowDTO>> getShowsForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getShowsForEvent(eventId));
    }

    @GetMapping("/shows/{showId}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long showId) {
        return ResponseEntity.ok(eventService.getShowById(showId));
    }

    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<ShowSeatDTO>> getAllSeatsForShow(@PathVariable Long showId) {
        return ResponseEntity.ok(eventService.getAllSeatsForShow(showId));
    }

    @GetMapping("/shows/{showId}/seats/available")
    public ResponseEntity<List<ShowSeatDTO>> getAvailableSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(eventService.getAvailableSeats(showId));
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(dto));
    }
}
