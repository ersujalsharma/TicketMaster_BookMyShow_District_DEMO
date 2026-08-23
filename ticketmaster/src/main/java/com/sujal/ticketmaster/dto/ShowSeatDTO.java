package com.sujal.ticketmaster.dto;

import com.sujal.ticketmaster.entity.SeatStatus;
import com.sujal.ticketmaster.entity.SeatType;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeatDTO {
    private Long id;
    private String seatNumber;
    private String seatRow;
    private SeatType seatType;
    private BigDecimal price;
    private SeatStatus status;
}
