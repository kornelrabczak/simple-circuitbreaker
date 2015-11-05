package com.thecookiezen.circuitbreaker;

import java.util.concurrent.Callable;

interface CircuitBreaker {
    void doCall(Callable<Void> callable) throws CircuitBreakerCallableFailure;
    boolean isOpen();
}


