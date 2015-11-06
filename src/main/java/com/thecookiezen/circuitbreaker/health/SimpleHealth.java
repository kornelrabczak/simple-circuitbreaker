package com.thecookiezen.circuitbreaker.health;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleHealth implements CircuitHealth {
    private volatile HealthCounts healthCountsSnapshot = new HealthCounts(0L, 0L, 0);
    private volatile AtomicLong lastHealthCountsSnapshot = new AtomicLong(System.currentTimeMillis());
    private final HealthConfiguration config;
    private AtomicInteger successCounter = new AtomicInteger(0);
    private AtomicInteger failureCounter = new AtomicInteger(0);

    public SimpleHealth(HealthConfiguration config) {
        this.config = config;
    }

    @Override
    public void resetCounter() {
        successCounter.set(0);
        failureCounter.set(0);
        this.lastHealthCountsSnapshot.set(System.currentTimeMillis());
        this.healthCountsSnapshot = new HealthCounts(0L, 0L, 0);
    }

    @Override
    public void markSuccess() {
        successCounter.incrementAndGet();
    }

    @Override
    public void markFailure() {
        failureCounter.incrementAndGet();
    }

    @Override
    public boolean shouldOpen() {
        HealthCounts health = getHealthCounts();
        if (health.getTotalRequests() < config.getRequestVolumeThreshold()) {
            return false;
        }

        return health.getErrorPercentage() >= config.getErrorThresholdPercentage();
    }

    public HealthCounts getHealthCounts() {
        long lastTime = this.lastHealthCountsSnapshot.get();
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastTime >= config.getHealthSnapshotIntervalInMilliseconds()
                || this.healthCountsSnapshot == null) && this.lastHealthCountsSnapshot.compareAndSet(lastTime, currentTime)) {
            long success = this.successCounter.get();
            long failure = this.failureCounter.get();
            long totalCount = failure + success;
            int errorPercentage = 0;
            if (totalCount > 0L) {
                errorPercentage = (int) ((double) failure / (double) totalCount * 100.0D);
            }

            this.healthCountsSnapshot = new HealthCounts(totalCount, failure, errorPercentage);
        }

        return this.healthCountsSnapshot;
    }
}
