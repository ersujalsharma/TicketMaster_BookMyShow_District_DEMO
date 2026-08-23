package com.sujal.ticketmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Show> shows;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Seat> seats;
}
