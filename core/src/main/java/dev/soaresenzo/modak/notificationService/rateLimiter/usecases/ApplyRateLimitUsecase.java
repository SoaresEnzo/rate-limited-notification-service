package dev.soaresenzo.modak.notificationService.rateLimiter.usecases;

import dev.soaresenzo.modak.notificationService.notification.Notification;


public interface ApplyRateLimitUsecase {

    void apply(Notification notification, Runnable function);
}
