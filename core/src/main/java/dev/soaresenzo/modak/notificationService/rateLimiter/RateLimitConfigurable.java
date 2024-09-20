package dev.soaresenzo.modak.notificationService.rateLimiter;

import java.time.temporal.ChronoUnit;

public interface RateLimitConfigurable {

    ChronoUnit getTimePeriod();
    Long getTimeLimit();
    Long getAmountOfRequests();
    String getKey();
}
