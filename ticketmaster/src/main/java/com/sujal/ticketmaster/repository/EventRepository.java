package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Event;
import com.sujal.ticketmaster.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByType(EventType type);

    @Query("SELECT DISTINCT e FROM Event e JOIN e.shows s JOIN s.venue v WHERE v.location.id = :locationId")
    List<Event> findEventsByLocationId(@Param("locationId") Long locationId);
}
