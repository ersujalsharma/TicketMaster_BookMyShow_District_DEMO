package com.sujal.ticketmaster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Long id;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
