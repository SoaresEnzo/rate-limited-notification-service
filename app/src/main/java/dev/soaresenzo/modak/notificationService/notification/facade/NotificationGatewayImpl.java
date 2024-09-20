package dev.soaresenzo.modak.notificationService.notification.facade;

import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.dataprovider.NotificationGateway;
import jakarta.inject.Named;

@Named
public class NotificationGatewayImpl implements NotificationGateway {
    @Override
    public void sendNotification(Notification notification) {
        System.out.println(STR."Sending notification: \{notification}");
    }
}
