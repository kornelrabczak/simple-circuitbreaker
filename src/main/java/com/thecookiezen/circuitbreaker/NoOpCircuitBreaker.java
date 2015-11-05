package com.thecookiezen.circuitbreaker;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.Callable;

@ApplicationScoped
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
    public void doOnOpen() {

    }

    @Override
    public void doOnClose() {

    }
}
