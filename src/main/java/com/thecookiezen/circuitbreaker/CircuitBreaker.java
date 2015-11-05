package com.thecookiezen.circuitbreaker;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CircuitBreaker {

    private static final ScheduledExecutorService closeCircuitBreakerScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private CircuitBreakerHealth circuitBreakerHealth = CircuitBreakerHealth.getInstance();
    private AtomicBoolean available = new AtomicBoolean(true);

    public void doCall(Callable<Void> callable) throws CircuitBreakerCallableFailure {
        if (!available.get()) {
            throw new CircuitBreakerCallableFailure("CircuitBreaker is OPEN");
        }

        try {
            callable.call();
            circuitBreakerHealth.markSuccess();
        } catch (Throwable ex) {
            circuitBreakerHealth.markFailure();
            onError(ex);
        }
    }

    private void onError(Throwable ex) throws CircuitBreakerCallableFailure {
        if (circuitBreakerHealth.shouldOpen() && available.compareAndSet(true, false)) {
            closeCircuitBreakerScheduledExecutor.schedule(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    available.set(true);
                    circuitBreakerHealth.resetCounter();
                    return null;
                }
            }, 5, TimeUnit.MINUTES);
        }

        throw new CircuitBreakerCallableFailure(ex);
    }
}
