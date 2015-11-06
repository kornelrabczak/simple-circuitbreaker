package com.thecookiezen.circuitbreaker.health;

public interface CircuitHealth {
    void resetCounter();
    void markSuccess();
    void markFailure();
    boolean shouldOpen();
}
