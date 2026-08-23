package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Booking;
import com.sujal.ticketmaster.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStatus(BookingStatus status);
}
