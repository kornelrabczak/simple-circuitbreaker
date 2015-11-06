package com.thecookiezen.circuitbreaker.health;

public class HealthConfiguration {
    private final int errorThresholdPercentage;
    private final int requestVolumeThreshold;
    private final int rollingStatisticalWindowInMilliseconds;
    private final int rollingStatisticalWindowBuckets;
    private final int HealthSnapshotIntervalInMilliseconds;

    public static HealthConfiguration getDefault() {
        return new HealthConfiguration(50, 20, 10000, 10, 500);
    }

    public HealthConfiguration(int errorThresholdPercentage, int requestVolumeThreshold,
                               int rollingStatisticalWindowInMilliseconds, int rollingStatisticalWindowBuckets, int healthSnapshotIntervalInMilliseconds) {
        this.errorThresholdPercentage = errorThresholdPercentage;
        this.requestVolumeThreshold = requestVolumeThreshold;
        this.rollingStatisticalWindowInMilliseconds = rollingStatisticalWindowInMilliseconds;
        this.rollingStatisticalWindowBuckets = rollingStatisticalWindowBuckets;
        HealthSnapshotIntervalInMilliseconds = healthSnapshotIntervalInMilliseconds;
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
