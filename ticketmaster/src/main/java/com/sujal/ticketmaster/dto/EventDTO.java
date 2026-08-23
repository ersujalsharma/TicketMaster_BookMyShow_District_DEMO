package com.sujal.ticketmaster.dto;

import com.sujal.ticketmaster.entity.EventType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private EventType type;
    private String language;
    private String posterUrl;
}
