package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByBookingId(Long bookingId);
    Optional<Ticket> findByTicketNumber(String ticketNumber);
}
