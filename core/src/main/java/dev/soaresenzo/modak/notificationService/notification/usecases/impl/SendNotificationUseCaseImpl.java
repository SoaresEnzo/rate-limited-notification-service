package dev.soaresenzo.modak.notificationService.notification.usecases.impl;

import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.dataprovider.NotificationGateway;
import dev.soaresenzo.modak.notificationService.notification.usecases.SendNotificationUseCase;
import dev.soaresenzo.modak.notificationService.rateLimiter.usecases.ApplyRateLimitUsecase;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Objects;

@Named
public class SendNotificationUseCaseImpl implements SendNotificationUseCase {
    private final NotificationGateway notificationGateway;
    private final ApplyRateLimitUsecase applyRateLimitUsecase;

    @Inject
    public SendNotificationUseCaseImpl(final NotificationGateway notificationGateway, final ApplyRateLimitUsecase applyRateLimitUsecase) {
        this.notificationGateway = Objects.requireNonNull(notificationGateway);
        this.applyRateLimitUsecase = Objects.requireNonNull(applyRateLimitUsecase);
    }

    @Override
    public void send(final Notification notification) {
        this.applyRateLimitUsecase
                .apply(
                        notification,
                        () -> this.notificationGateway.sendNotification(notification)
        );
    }
}
