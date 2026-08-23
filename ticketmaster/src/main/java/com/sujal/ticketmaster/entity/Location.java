package com.sujal.ticketmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    private String state;

    private String country;

    private String zipCode;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Venue> venues;
}
