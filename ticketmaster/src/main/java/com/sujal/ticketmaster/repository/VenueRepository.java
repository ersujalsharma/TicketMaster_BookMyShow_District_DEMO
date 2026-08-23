package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByLocationId(Long locationId);
    List<Venue> findByNameContainingIgnoreCase(String name);
}
