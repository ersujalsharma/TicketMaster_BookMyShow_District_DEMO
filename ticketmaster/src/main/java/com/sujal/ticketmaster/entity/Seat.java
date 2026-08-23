package com.sujal.ticketmaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "seat_row")
    private String seatRow;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    private SeatType seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}
