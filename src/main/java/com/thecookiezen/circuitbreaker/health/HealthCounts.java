package com.thecookiezen.circuitbreaker.health;

public class HealthCounts {
    private final long totalCount;
    private final long errorCount;
    private final int errorPercentage;

    public HealthCounts(long total, long error, int errorPercentage) {
        this.totalCount = total;
        this.errorCount = error;
        this.errorPercentage = errorPercentage;
    }

    public long getTotalRequests() {
        return this.totalCount;
    }

    public long getErrorCount() {
        return this.errorCount;
    }

    public int getErrorPercentage() {
        return this.errorPercentage;
    }
}
