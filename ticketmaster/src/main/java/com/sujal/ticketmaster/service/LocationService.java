package com.sujal.ticketmaster.service;

import com.sujal.ticketmaster.dto.LocationDTO;
import com.sujal.ticketmaster.entity.Location;
import com.sujal.ticketmaster.exception.ResourceNotFoundException;
import com.sujal.ticketmaster.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return toDTO(location);
    }

    public List<LocationDTO> searchByCity(String city) {
        return locationRepository.findByCityContainingIgnoreCase(city).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> searchByState(String state) {
        return locationRepository.findByStateContainingIgnoreCase(state).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO createLocation(LocationDTO dto) {
        Location location = Location.builder()
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .zipCode(dto.getZipCode())
                .build();
        Location saved = locationRepository.save(location);
        return toDTO(saved);
    }

    private LocationDTO toDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .city(location.getCity())
                .state(location.getState())
                .country(location.getCountry())
                .zipCode(location.getZipCode())
                .build();
    }
}
