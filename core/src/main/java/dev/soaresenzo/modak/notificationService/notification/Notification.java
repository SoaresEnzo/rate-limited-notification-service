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
    private final String subject;
    private final NotificationType type;
    private final NotificationChannel channel;
    private final EmailAddress recipient;

    private Notification(final NotificationID id, final String body, final String subject, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        this.id = id;
        this.body = body;
        this.subject = subject;
        this.type = type;
        this.channel = channel;
        this.recipient = recipient;
    }

    public static Notification newNotification(final String body, final String subject, final NotificationType type, final NotificationChannel channel, final EmailAddress recipient) {
        final var notification = new Notification(NotificationID.unique(), body, subject, type, channel, recipient);
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

        if(Objects.isNull(this.subject) || this.subject.isEmpty()) {
            throw new NotificationException("Notifications must contain a subject.");
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

    public String getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notification that = (Notification) o;
        return Objects.equals(body, that.body) && Objects.equals(subject, that.subject) && Objects.equals(type, that.type) && channel == that.channel && Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body, type, channel, recipient);
    }
}
