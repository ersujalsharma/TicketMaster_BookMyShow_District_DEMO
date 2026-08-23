package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByEventId(Long eventId);
    List<Show> findByVenueId(Long venueId);
    List<Show> findByVenueIdAndDate(Long venueId, LocalDate date);
    List<Show> findByEventIdAndVenueId(Long eventId, Long venueId);
}
