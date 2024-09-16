package dev.soaresenzo.modak.notificationService.notification.dataprovider;

import dev.soaresenzo.modak.notificationService.notification.Notification;

public interface NotificationGateway {
    void sendNotification(Notification notification);
}
