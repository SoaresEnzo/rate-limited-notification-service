package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest;

import dev.soaresenzo.modak.notificationService.configuration.exception.MisconfigurationException;
import dev.soaresenzo.modak.notificationService.configuration.exception.NotConfiguredException;
import dev.soaresenzo.modak.notificationService.configuration.parameters.NotificationConfiguration;
import dev.soaresenzo.modak.notificationService.configuration.parameters.NotificationConfigurationImpl;
import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests.SendNotificationRequest;
import dev.soaresenzo.modak.notificationService.notification.usecases.SendNotificationUseCase;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import jakarta.inject.Inject;
import jdk.jfr.ContentType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

@RestController
public class NotificationRestController implements NotificationRestEndpoint {
    private final SendNotificationUseCase sendNotificationUseCase;
    private final NotificationConfiguration notificationConfig;

    @Inject
    public NotificationRestController(final SendNotificationUseCase sendNotificationUseCase, final NotificationConfiguration notificationConfig) {
        this.sendNotificationUseCase = Objects.requireNonNull(sendNotificationUseCase);
        this.notificationConfig = Objects.requireNonNull(notificationConfig);
    }

    @Override
    public ResponseEntity<?> sendNotification(SendNotificationRequest request) {
        final var rateLimitProperty = this.notificationConfig.getRateLimitConfig(request.type());
        if (rateLimitProperty == null) {
            throw new NotConfiguredException(STR."Notification type not found. Available types: \{this.notificationConfig.getAvailableTypes()}");
        }

        NotificationChannel channel;

        try {
            channel = NotificationChannel.valueOf(request.channel());
        } catch (IllegalArgumentException e) {
            throw new NotConfiguredException(STR."Notification channel not implemented. Available channels: \{Arrays.toString(NotificationChannel.values())}");
        }

        ChronoUnit period;

        try {
            period = ChronoUnit.valueOf(rateLimitProperty.period());
        } catch (IllegalArgumentException e) {
            throw new MisconfigurationException(STR."Notification period not configured. Available periods: \{Arrays.toString(ChronoUnit.values())}");
        }

        final var notificationType = new NotificationType(
                request.type(),
                period,
                (long) rateLimitProperty.limit(),
                (long) rateLimitProperty.rate()
        );

        final var notification = Notification
                .newNotification(request.body(),
                        request.subject(),
                        notificationType,
                        channel,
                        EmailAddress.of(request.recipient())
                );
        this.sendNotificationUseCase.send(notification);
        return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON).build();
    }
}
