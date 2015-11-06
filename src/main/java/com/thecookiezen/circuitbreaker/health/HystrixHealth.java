package com.thecookiezen.circuitbreaker.health;

import com.netflix.hystrix.util.HystrixRollingNumber;
import com.netflix.hystrix.util.HystrixRollingNumberEvent;

import java.util.concurrent.atomic.AtomicLong;

public class HystrixHealth implements CircuitHealth {
    private final HealthConfiguration config;
    private volatile HealthCounts healthCountsSnapshot = new HealthCounts(0L, 0L, 0);
    private volatile AtomicLong lastHealthCountsSnapshot = new AtomicLong(System.currentTimeMillis());
    private final HystrixRollingNumber counter;

    public HystrixHealth(HealthConfiguration config) {
        this.counter = new HystrixRollingNumber(config::getRollingStatisticalWindowInMilliseconds, config::getRollingStatisticalWindowBuckets);
        this.config = config;
    }

    @Override
    public void resetCounter() {
        this.counter.reset();
        this.lastHealthCountsSnapshot.set(System.currentTimeMillis());
        this.healthCountsSnapshot = new HealthCounts(0L, 0L, 0);
    }

    @Override
    public void markSuccess() {
        this.counter.increment(HystrixRollingNumberEvent.SUCCESS);
    }

    @Override
    public void markFailure() {
        this.counter.increment(HystrixRollingNumberEvent.FAILURE);
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
            long success = this.counter.getRollingSum(HystrixRollingNumberEvent.SUCCESS);
            long failure = this.counter.getRollingSum(HystrixRollingNumberEvent.FAILURE);
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
