package com.sujal.ticketmaster.controller;

import com.sujal.ticketmaster.dto.ShowDTO;
import com.sujal.ticketmaster.dto.VenueDTO;
import com.sujal.ticketmaster.service.EventService;
import com.sujal.ticketmaster.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<VenueDTO>> getVenuesByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(venueService.getVenuesByLocation(locationId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VenueDTO>> searchVenues(@RequestParam String name) {
        return ResponseEntity.ok(venueService.searchVenues(name));
    }

    @GetMapping("/{venueId}/shows")
    public ResponseEntity<List<ShowDTO>> getShowsForVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(eventService.getShowsForVenue(venueId));
    }

    @PostMapping
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.createVenue(dto));
    }
}
