package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest;

import dev.soaresenzo.modak.notificationService.configuration.exception.MisconfigurationException;
import dev.soaresenzo.modak.notificationService.configuration.exception.NotConfiguredException;
import dev.soaresenzo.modak.notificationService.configuration.parameters.NotificationConfig;
import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests.SendNotificationRequest;
import dev.soaresenzo.modak.notificationService.notification.usecases.SendNotificationUseCase;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import jakarta.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

@RestController
public class NotificationRestController implements NotificationRestEndpoint {
    private final SendNotificationUseCase sendNotificationUseCase;
    private final NotificationConfig notificationConfig;

    @Inject
    public NotificationRestController(final SendNotificationUseCase sendNotificationUseCase, final NotificationConfig notificationConfig) {
        this.sendNotificationUseCase = Objects.requireNonNull(sendNotificationUseCase);
        this.notificationConfig = Objects.requireNonNull(notificationConfig);
    }

    @Override
    public ResponseEntity<?> sendNotification(SendNotificationRequest request) {
        final var rateLimitProperty = this.notificationConfig.getRateLimitConfig(request.type());
        if (rateLimitProperty == null) {
            throw new NotConfiguredException(STR."Notification type not found. Available types: \{this.notificationConfig.getPropertiesMap().keySet()}");
        }

        NotificationChannel channel;

        try {
            channel = NotificationChannel.valueOf(request.channel());
        } catch (IllegalArgumentException e) {
            throw new NotConfiguredException("Notification channel not implemented. Available channels: EMAIL");
        }

        ChronoUnit period;

        try {
            period = ChronoUnit.valueOf(rateLimitProperty.getPeriod());
        } catch (IllegalArgumentException e) {
            throw new MisconfigurationException("Notification period not configured. Available periods: SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS");
        }

        final var notificationType = new NotificationType(
                request.type(),
                period,
                (long) rateLimitProperty.getLimit(),
                (long) rateLimitProperty.getRate()
        );

        final var notification = Notification
                .newNotification(request.body(),
                        notificationType,
                        channel,
                        EmailAddress.of(request.recipient())
                );
        this.sendNotificationUseCase.send(notification);
        return null;
    }
}
