package dev.soaresenzo.modak.notificationService.notification.facade;

import dev.soaresenzo.modak.notificationService.configuration.mail.EmailService;
import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.dataprovider.NotificationGateway;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


@Named
public class NotificationGatewayImpl implements NotificationGateway {
    private static Logger logger = LoggerFactory.getLogger(NotificationGatewayImpl.class);
    private final EmailService emailService;

    @Inject
    public NotificationGatewayImpl(EmailService emailService) {
        this.emailService = Objects.requireNonNull(emailService);
    }

    @Override
    public void sendNotification(Notification notification) {

        this.emailService.sendEmail(
                notification.getRecipient().getSubject(),
                notification.getSubject(),
                notification.getBody()
        );
    }
}
