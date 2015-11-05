package com.thecookiezen.circuitbreaker;

public class CircuitBreakerCallableFailure extends Exception {
    private static final long serialVersionUID = 1L;

    public CircuitBreakerCallableFailure(Throwable cause) {
        super(cause);
    }

    public CircuitBreakerCallableFailure(String message) {
        super(message);
    }
}
