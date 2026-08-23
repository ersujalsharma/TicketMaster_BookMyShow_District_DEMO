package com.sujal.ticketmaster.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}
