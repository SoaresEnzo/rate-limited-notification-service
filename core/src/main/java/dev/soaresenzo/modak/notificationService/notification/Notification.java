package dev.soaresenzo.modak.notificationService.notification;

import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationStatus;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;

public class Notification {
    private String body;
    private NotificationStatus status;
    private NotificationType type;
    private NotificationChannel channel;
    private EmailAddress recipient;

    private Notification(final String body, final NotificationStatus status, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        this.body = body;
        this.status = status;
        this.type = type;
        this.channel = channel;
        this.recipient = recipient;
    }

    public static Notification of(final String body, final NotificationStatus status, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        final var notification = new Notification(body, status, type, channel, recipient);
        notification.validate();
        return notification;
    }

    public void validate() {

    }


}
