package com.sujal.ticketmaster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDTO {
    private Long id;
    private String name;
    private String address;
    private Integer totalSeats;
    private Long locationId;
    private String city;
}
