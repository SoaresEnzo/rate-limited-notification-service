package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests;

public record SendNotificationRequest(
        String channel,
        String type,
        String recipient,
        String body
) {
}
