package dev.soaresenzo.modak.notificationService.notification.usecases;

import dev.soaresenzo.modak.notificationService.notification.Notification;

public interface SendNotificationUseCase {

    void send(final Notification notification);
}
