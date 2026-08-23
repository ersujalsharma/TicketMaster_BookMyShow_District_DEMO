package com.sujal.ticketmaster.repository;

import com.sujal.ticketmaster.entity.SeatStatus;
import com.sujal.ticketmaster.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowIdAndStatus(Long showId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ss FROM ShowSeat ss WHERE ss.id = :id")
    Optional<ShowSeat> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ss FROM ShowSeat ss WHERE ss.id IN :ids")
    List<ShowSeat> findAllByIdWithLock(@Param("ids") List<Long> ids);
}
