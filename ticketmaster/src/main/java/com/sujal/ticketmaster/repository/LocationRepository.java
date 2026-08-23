package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCityContainingIgnoreCase(String city);
    List<Location> findByStateContainingIgnoreCase(String state);
}
