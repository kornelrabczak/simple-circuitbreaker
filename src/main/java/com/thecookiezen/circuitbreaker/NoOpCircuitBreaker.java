package com.thecookiezen.circuitbreaker;

import java.util.concurrent.Callable;

public class NoOpCircuitBreaker implements CircuitBreaker {
    @Override
    public void doCall(Callable<Void> callable) throws CircuitBreakerCallableFailure {
        try {
            callable.call();
        } catch (Exception e) {
            throw new CircuitBreakerCallableFailure(e);
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
