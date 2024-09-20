package dev.soaresenzo.modak.notificationService.notification;

import dev.soaresenzo.modak.notificationService.Entity;
import dev.soaresenzo.modak.notificationService.notification.exceptions.NotificationException;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationID;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;

import java.util.Objects;

public class Notification extends Entity<NotificationID> {
    private final String body;
    private final NotificationType type;
    private final NotificationChannel channel;
    private final EmailAddress recipient;

    private Notification(final NotificationID id, final String body, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        this.id = id;
        this.body = body;
        this.type = type;
        this.channel = channel;
        this.recipient = recipient;
    }

    public static Notification newNotification(final String body, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        final var notification = new Notification(NotificationID.unique(), body, type, channel, recipient);
        notification.validate();
        return notification;
    }

    public RateLimitConfigurable getRateLimitData() {
        return this.type;
    }

    @Override
    public void validate() {
        if(Objects.isNull(this.body) || this.body.isEmpty()) {
            throw new NotificationException("Notifications must contain a body.");
        }

        if(Objects.isNull(this.recipient)) {
            throw new NotificationException("Notifications must contain a recipient.");
        }

        if(Objects.isNull(this.type)) {
            throw new NotificationException("Notifications must contain a type.");
        }

        if(Objects.isNull(this.channel)) {
            throw new NotificationException("Notifications must contain a channel.");
        }

    }

    public String getBody() {
        return body;
    }

    public NotificationType getType() {
        return type;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public EmailAddress getRecipient() {
        return recipient;
    }
}
