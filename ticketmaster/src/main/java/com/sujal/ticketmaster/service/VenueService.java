package com.sujal.ticketmaster.service;

import com.sujal.ticketmaster.dto.VenueDTO;
import com.sujal.ticketmaster.entity.Location;
import com.sujal.ticketmaster.entity.Venue;
import com.sujal.ticketmaster.exception.ResourceNotFoundException;
import com.sujal.ticketmaster.repository.LocationRepository;
import com.sujal.ticketmaster.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final LocationRepository locationRepository;

    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VenueDTO getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        return toDTO(venue);
    }

    public List<VenueDTO> getVenuesByLocation(Long locationId) {
        return venueRepository.findByLocationId(locationId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VenueDTO> searchVenues(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VenueDTO createVenue(VenueDTO dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        Venue venue = Venue.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .totalSeats(dto.getTotalSeats())
                .location(location)
                .build();
        Venue saved = venueRepository.save(venue);
        return toDTO(saved);
    }

    private VenueDTO toDTO(Venue venue) {
        return VenueDTO.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .totalSeats(venue.getTotalSeats())
                .locationId(venue.getLocation().getId())
                .city(venue.getLocation().getCity())
                .build();
    }
}
