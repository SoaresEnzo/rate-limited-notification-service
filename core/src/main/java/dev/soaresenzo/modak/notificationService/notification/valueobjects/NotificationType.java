package dev.soaresenzo.modak.notificationService.notification.valueobjects;

import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitable;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class NotificationType implements RateLimitable {
    private final String typeName;
    private final ChronoUnit rateLimitPeriod;
    private final Long rateLimitLimit;
    private final Long rateLimitAmountOfRequests;

    public NotificationType(final String typeName, final ChronoUnit rateLimitPeriod, final Long rateLimitLimit, final Long rateLimitAmountOfRequests) {
        this.typeName = typeName;
        this.rateLimitPeriod = rateLimitPeriod;
        this.rateLimitLimit = rateLimitLimit;
        this.rateLimitAmountOfRequests = rateLimitAmountOfRequests;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public ChronoUnit getTimePeriod() {
        return this.rateLimitPeriod;
    }

    @Override
    public Long getTimeLimit() {
        return this.rateLimitLimit;
    }

    @Override
    public Long getAmountOfRequests() {
        return this.rateLimitAmountOfRequests;
    }

    @Override
    public String getKey() {
        return this.getTypeName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationType that = (NotificationType) o;
        return Objects.equals(typeName, that.typeName) && rateLimitPeriod == that.rateLimitPeriod && Objects.equals(rateLimitLimit, that.rateLimitLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, rateLimitPeriod, rateLimitLimit);
    }
}
