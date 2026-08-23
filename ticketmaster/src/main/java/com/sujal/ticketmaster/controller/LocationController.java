package com.sujal.ticketmaster.controller;

import com.sujal.ticketmaster.dto.LocationDTO;
import com.sujal.ticketmaster.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @GetMapping("/search/city")
    public ResponseEntity<List<LocationDTO>> searchByCity(@RequestParam String city) {
        return ResponseEntity.ok(locationService.searchByCity(city));
    }

    @GetMapping("/search/state")
    public ResponseEntity<List<LocationDTO>> searchByState(@RequestParam String state) {
        return ResponseEntity.ok(locationService.searchByState(state));
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(dto));
    }
}
