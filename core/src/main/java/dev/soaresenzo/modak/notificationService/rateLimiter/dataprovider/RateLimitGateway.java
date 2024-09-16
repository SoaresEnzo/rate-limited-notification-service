package dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider;

import dev.soaresenzo.modak.notificationService.Identifier;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitable;

public interface RateLimitGateway {

    Integer getAmountOfRequestsInTheLastPeriodForRecipient(EmailAddress recipient, RateLimitable limitable);
    void saveRequest(Identifier id, EmailAddress recipient, RateLimitable limitable);
}
