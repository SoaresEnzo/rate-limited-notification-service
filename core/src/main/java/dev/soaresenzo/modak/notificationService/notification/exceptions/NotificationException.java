package dev.soaresenzo.modak.notificationService.notification.exceptions;

public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message, null, true, false);
    }
}
