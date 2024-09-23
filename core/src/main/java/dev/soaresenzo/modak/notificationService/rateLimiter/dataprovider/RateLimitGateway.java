package dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider;

import dev.soaresenzo.modak.notificationService.Identifier;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;

public interface RateLimitGateway {

    Long getAmountOfRequestsInTheLastPeriodForRecipient(RateLimitSubject subject, RateLimitConfigurable configurable);
    void saveRequest(Identifier id, RateLimitSubject subject, RateLimitConfigurable configurable);
    void removeRequest(Identifier id, RateLimitSubject subject, RateLimitConfigurable configurable);
}
