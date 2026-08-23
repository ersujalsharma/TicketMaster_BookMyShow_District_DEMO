package com.sujal.ticketmaster.controller;

import com.sujal.ticketmaster.dto.PaymentRequest;
import com.sujal.ticketmaster.dto.PaymentResponse;
import com.sujal.ticketmaster.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }
}
