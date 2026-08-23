package com.sujal.ticketmaster.exception;

public class SeatLockException extends RuntimeException {
    public SeatLockException(String message) {
        super(message);
    }
}
