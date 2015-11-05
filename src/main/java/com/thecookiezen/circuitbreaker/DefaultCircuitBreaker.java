package com.thecookiezen.circuitbreaker;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultCircuitBreaker implements CircuitBreaker {

    private static final ScheduledExecutorService closeCircuitBreakerScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final CircuitBreakerHealth circuitBreakerHealth;
    private final AtomicBoolean available = new AtomicBoolean(true);
    private final long closeCircuitDelay;
    private final TimeUnit closeCircuitDelayTimeUnit;

    public DefaultCircuitBreaker(long closeCircuitDelay, TimeUnit closeCircuitDelayTimeUnit) {
        this.closeCircuitDelay = closeCircuitDelay;
        this.closeCircuitDelayTimeUnit = closeCircuitDelayTimeUnit;
        this.circuitBreakerHealth = CircuitBreakerHealth.getInstance(CircuitBreakerHealthConfiguration.getDefault());
    }

    public DefaultCircuitBreaker(long closeCircuitDelay, TimeUnit closeCircuitDelayTimeUnit, CircuitBreakerHealthConfiguration config) {
        this.closeCircuitDelay = closeCircuitDelay;
        this.closeCircuitDelayTimeUnit = closeCircuitDelayTimeUnit;
        this.circuitBreakerHealth = CircuitBreakerHealth.getInstance(config);
    }

    @Override
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
                public Void call() throws Exception {
                    available.set(true);
                    circuitBreakerHealth.resetCounter();
                    return null;
                }
            }, closeCircuitDelay, closeCircuitDelayTimeUnit);
        }

        throw new CircuitBreakerCallableFailure(ex);
    }

    @Override
    public boolean isOpen() {
        return !available.get();
    }
}
