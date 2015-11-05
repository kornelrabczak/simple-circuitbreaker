package com.thecookiezen.circuitbreaker;

import org.junit.Test;

public class NoOpCircuitBreakerTest {

    private CircuitBreaker sut = new NoOpCircuitBreaker();

    @Test
    public void shouldExecuteCallable() throws CircuitBreakerCallableFailure {
        sut.doCall(() -> null);
    }

    @Test(expected = CircuitBreakerCallableFailure.class)
    public void shouldThrowExceptionWhenExecuteCallable() throws CircuitBreakerCallableFailure {
        sut.doCall(() -> {
            throw new RuntimeException("failed");
        });
    }
}