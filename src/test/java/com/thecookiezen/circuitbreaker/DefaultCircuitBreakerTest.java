package com.thecookiezen.circuitbreaker;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCircuitBreakerTest {

    private CircuitBreaker sut = new DefaultCircuitBreaker(5, TimeUnit.SECONDS, new CircuitBreakerHealthConfiguration("test", 50, 1, 10000, 10, 1));

    @Test
    public void shouldExecuteCallableAndStayClosed() throws CircuitBreakerCallableFailure {
        sut.doCall(() -> null);

        assertThat(sut.isOpen()).isFalse();
    }

    @Test
    public void shouldOpenAndAfter5SecondsClosed() throws InterruptedException, CircuitBreakerCallableFailure {
        assertThat(sut.isOpen()).isFalse();

        try {
            sut.doCall(() -> {
                throw new RuntimeException("failed");
            });
        } catch (CircuitBreakerCallableFailure ignore) {}

        assertThat(sut.isOpen()).isTrue();

        await().atMost(10, TimeUnit.SECONDS).until(() -> !sut.isOpen());
    }

    @Test
    public void shouldOpenAndThrowExceptionWithMessage() throws InterruptedException, CircuitBreakerCallableFailure {
        assertThat(sut.isOpen()).isFalse();

        try {
            sut.doCall(() -> {
                throw new RuntimeException("failed");
            });
        } catch (CircuitBreakerCallableFailure ignore) {}

        assertThat(sut.isOpen()).isTrue();

        catchException(sut).doCall(() -> null);

        Exception exception = caughtException();
        assertThat(exception).isInstanceOf(CircuitBreakerCallableFailure.class);
        assertThat(exception).hasMessage("CircuitBreaker is OPEN");

        await().atMost(10, TimeUnit.SECONDS).until(() -> !sut.isOpen());

        sut.doCall(() -> null);
    }

}