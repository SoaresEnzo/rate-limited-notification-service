package dev.soaresenzo.modak.notificationService.notification;

import dev.soaresenzo.modak.notificationService.notification.exceptions.NotificationException;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

class NotificationTest {

    @Test
    void givenAValidInput_whenCallNewNotification_thenShouldReturnANotification() {
        final var aBody = "email body";
        final var aRecipient = EmailAddress.of("test@gmail.com");
        final var aType = new NotificationType("MARKETING", ChronoUnit.DAYS, 1L, 10L);

        final var aNotification = Notification.newNotification(aBody, aType, NotificationChannel.EMAIL, aRecipient);

        Assertions.assertEquals(aBody, aNotification.getBody());
        Assertions.assertEquals(aRecipient, aNotification.getRecipient());
        Assertions.assertEquals(aType, aNotification.getType());
        Assertions.assertEquals(NotificationChannel.EMAIL, aNotification.getChannel());
        Assertions.assertNotNull(aNotification.getId().getValue());
    }

    @Test
    void givenAnInvalidBody_whenCallNewNotification_thenShouldThrowAnException() {
        final var aBody = "";
        final var aRecipient = EmailAddress.of("test@gmail.com");
        final var aType = new NotificationType("MARKETING", ChronoUnit.DAYS, 1L, 10L);

        final var anException = Assertions.assertThrows(NotificationException.class, ()-> Notification.newNotification(aBody, aType, NotificationChannel.EMAIL, aRecipient));

        Assertions.assertEquals("Notifications must contain a body.", anException.getMessage());
    }

    @Test
    void givenAnInvalidType_whenCallNewNotification_thenShouldThrowAnException() {
        final var aBody = "A valid body";
        final var aRecipient = EmailAddress.of("test@gmail.com");
        final NotificationType aType = null;

        final var anException = Assertions.assertThrows(NotificationException.class, ()-> Notification.newNotification(aBody, aType, NotificationChannel.EMAIL, aRecipient));

        Assertions.assertEquals("Notifications must contain a type.", anException.getMessage());
    }

    @Test
    void givenAnInvalidEmail_whenCallNewNotification_thenShouldThrowAnException() {
        final var aBody = "A valid body";
        final EmailAddress aRecipient = null;
        final var aType = new NotificationType("MARKETING", ChronoUnit.DAYS, 1L, 10L);

        final var anException = Assertions.assertThrows(NotificationException.class, ()-> Notification.newNotification(aBody, aType, NotificationChannel.EMAIL, aRecipient));

        Assertions.assertEquals("Notifications must contain a recipient.", anException.getMessage());
    }
}