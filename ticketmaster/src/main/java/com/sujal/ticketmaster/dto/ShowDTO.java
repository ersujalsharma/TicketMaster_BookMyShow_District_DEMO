package com.sujal.ticketmaster.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowDTO {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long eventId;
    private String eventName;
    private Long venueId;
    private String venueName;
}
