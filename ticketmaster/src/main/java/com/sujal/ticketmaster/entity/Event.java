package com.sujal.ticketmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String language;

    private String posterUrl;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Show> shows;
}
