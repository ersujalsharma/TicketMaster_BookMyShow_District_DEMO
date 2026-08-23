package com.sujal.ticketmaster.dto;

import com.sujal.ticketmaster.entity.PaymentMethod;
import com.sujal.ticketmaster.entity.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long paymentId;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime paidAt;
    private String ticketNumber;
}
