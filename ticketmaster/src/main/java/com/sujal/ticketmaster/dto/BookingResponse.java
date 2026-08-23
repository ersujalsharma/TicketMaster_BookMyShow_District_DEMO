package com.sujal.ticketmaster.dto;

import com.sujal.ticketmaster.entity.BookingStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long showId;
    private String eventName;
    private String venueName;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private List<ShowSeatDTO> seats;
    private LocalDateTime bookedAt;
    private String ticketNumber;
}
