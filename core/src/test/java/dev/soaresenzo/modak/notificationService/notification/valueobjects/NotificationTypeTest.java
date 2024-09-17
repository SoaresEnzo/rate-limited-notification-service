package dev.soaresenzo.modak.notificationService.notification.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTypeTest {

    @Test
    void givenAValidNotificationType_whenInstantiatingClass_shouldReturnObject() {
        final var aNotificationType = new NotificationType("MARKETING", ChronoUnit.DAYS, 1L, 10L);

        assertEquals("MARKETING", aNotificationType.getTypeName());
        assertEquals(10L, aNotificationType.getAmountOfRequests());
        assertEquals(1L, aNotificationType.getTimeLimit());
        assertEquals(ChronoUnit.DAYS, aNotificationType.getTimePeriod());
    }
}