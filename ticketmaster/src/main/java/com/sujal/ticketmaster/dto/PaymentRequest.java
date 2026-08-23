package com.sujal.ticketmaster.dto;

import com.sujal.ticketmaster.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
