package com.thecookiezen.circuitbreaker;

public class CircuitBreakerHealthConfiguration {
    private final String key;
    private final int errorThresholdPercentage;
    private final int requestVolumeThreshold;
    private final int rollingStatisticalWindowInMilliseconds;
    private final int rollingStatisticalWindowBuckets;
    private final int HealthSnapshotIntervalInMilliseconds;

    public static CircuitBreakerHealthConfiguration getDefault() {
        return new CircuitBreakerHealthConfiguration("simple-circuitbreaker", 50, 20, 10000, 10, 500);
    }

    public CircuitBreakerHealthConfiguration(String key, int errorThresholdPercentage, int requestVolumeThreshold,
                                             int rollingStatisticalWindowInMilliseconds, int rollingStatisticalWindowBuckets, int healthSnapshotIntervalInMilliseconds) {
        this.key = key;
        this.errorThresholdPercentage = errorThresholdPercentage;
        this.requestVolumeThreshold = requestVolumeThreshold;
        this.rollingStatisticalWindowInMilliseconds = rollingStatisticalWindowInMilliseconds;
        this.rollingStatisticalWindowBuckets = rollingStatisticalWindowBuckets;
        HealthSnapshotIntervalInMilliseconds = healthSnapshotIntervalInMilliseconds;
    }

    public String getKey() {
        return key;
    }

    public int getErrorThresholdPercentage() {
        return errorThresholdPercentage;
    }

    public int getRollingStatisticalWindowInMilliseconds() {
        return rollingStatisticalWindowInMilliseconds;
    }

    public int getRequestVolumeThreshold() {
        return requestVolumeThreshold;
    }

    public int getRollingStatisticalWindowBuckets() {
        return rollingStatisticalWindowBuckets;
    }

    public int getHealthSnapshotIntervalInMilliseconds() {
        return HealthSnapshotIntervalInMilliseconds;
    }
}
