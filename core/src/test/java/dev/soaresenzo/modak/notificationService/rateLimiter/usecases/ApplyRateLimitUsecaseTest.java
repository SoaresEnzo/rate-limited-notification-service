package dev.soaresenzo.modak.notificationService.rateLimiter.usecases;

import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.notification.dataprovider.NotificationGateway;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationChannel;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider.RateLimitGateway;
import dev.soaresenzo.modak.notificationService.rateLimiter.exceptions.RateLimitExceededException;
import dev.soaresenzo.modak.notificationService.rateLimiter.usecases.impl.ApplyRateLimitUsecaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class ApplyRateLimitUsecaseTest {

    @InjectMocks
    private ApplyRateLimitUsecaseImpl applyRateLimitUsecase;

    @Mock
    private RateLimitGateway rateLimitGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(rateLimitGateway);
        Mockito.reset(notificationGateway);
    }

    @Test
    void givenANotification_whenRateLimited_thenShouldThrowException() {
        final var aNotification = Notification.newNotification(
                "test",
                "test",
                new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L),
                NotificationChannel.EMAIL,
                EmailAddress.of("test@gmail.com")
        );

        Mockito.when(rateLimitGateway.getAmountOfRequestsInTheLastPeriodForRecipient(
                aNotification.getRecipient(),
                aNotification.getRateLimitData()
        )).thenReturn(10L);

        final var anException = Assertions.assertThrows(RateLimitExceededException.class, ()->
            this.applyRateLimitUsecase.apply(aNotification, Assertions::fail)
        );

        Assertions.assertEquals("Too many requests.", anException.getMessage());
    }

    @Test
    void givenANotification_whenNotRateLimited_thenShouldExecuteFunction() {
        final var aNotification = Notification.newNotification(
                "test",
                "test",
                new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L),
                NotificationChannel.EMAIL,
                EmailAddress.of("test@gmail.com")
        );

        Mockito.when(rateLimitGateway.getAmountOfRequestsInTheLastPeriodForRecipient(
                aNotification.getRecipient(),
                aNotification.getRateLimitData()
        )).thenReturn(9L);

        Assertions.assertDoesNotThrow(()->
                this.applyRateLimitUsecase.apply(aNotification, ()-> notificationGateway.sendNotification(aNotification))
        );

        Mockito.verify(notificationGateway, Mockito.times(1)).sendNotification(aNotification);
        Mockito.verify(rateLimitGateway, Mockito.times(1)).saveRequest(aNotification.getId(), aNotification.getRecipient(), aNotification.getRateLimitData());
    }

    @Test
    void givenANotification_whenGateayThrowsRandomException_thenShouldThrowException() {
        final var aNotification = Notification.newNotification(
                "test",
                "test",
                new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L),
                NotificationChannel.EMAIL,
                EmailAddress.of("test@gmail.com")
        );
        final var anErrorThrown = "Random exception";

        Mockito.when(rateLimitGateway.getAmountOfRequestsInTheLastPeriodForRecipient(
                Mockito.any(),
                Mockito.any()
        )).thenThrow(new RuntimeException(anErrorThrown));

        final var anException = Assertions.assertThrows(RuntimeException.class, ()->
                this.applyRateLimitUsecase.apply(aNotification, Assertions::fail)
        );

        Assertions.assertEquals(anErrorThrown, anException.getMessage());
    }
}