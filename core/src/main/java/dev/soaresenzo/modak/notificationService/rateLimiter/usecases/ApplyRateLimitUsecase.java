package dev.soaresenzo.modak.notificationService.rateLimiter.usecases;

import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitable;


public interface ApplyRateLimitUsecase {

    void apply(Notification notification, Runnable function);
}
