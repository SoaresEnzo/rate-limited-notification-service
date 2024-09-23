package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.RedisCleanUpExtension;
import dev.soaresenzo.modak.notificationService.TestRedisConfiguration;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.EmailAddress;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationID;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestRedisConfiguration.class})
@ExtendWith(RedisCleanUpExtension.class)
@ActiveProfiles("test")
class RateLimitGatewayImplTest {

    @Inject
    private RateLimitGatewayImpl rateLimitGateway;

    @Inject
    private RedisTemplate<String, RateLimitStoreEntry> redisTemplate;

    @Test
    void givenAValidRequest_whenCallSaveRequest_shouldSaveRequest() {
        // given
        final var id = NotificationID.unique();
        final var subject = EmailAddress.of("test@test.com");
        final var configurable = new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L);

        // when
        rateLimitGateway.saveRequest(id, subject, configurable);

        // then
        final var key = RateLimitKey.generate(configurable, subject);
        final var entries = redisTemplate.opsForZSet().range(key, 0, -1);
        assertNotNull(entries);
        assertEquals(1, entries.size());
        final var entry = entries.iterator().next();
        assertEquals(id.getValue(), entry.getId());
    }

    @Test
    void givenSomePreSavedRequests_whenCallgetAmountOfRequestsInTheLastPeriodForRecipient_shouldReturnNumberOfPreSavedRequests() {
        // given
        final var subject = EmailAddress.of("test@test.com");
        final var configurable = new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L);
        final var expectedNumberOfRequests = 5;

        for (int i = 0; i < expectedNumberOfRequests; i++) {
            rateLimitGateway.saveRequest(NotificationID.unique(), subject, configurable);
        }

        // when
        rateLimitGateway.getAmountOfRequestsInTheLastPeriodForRecipient(subject, configurable);

        // then
        final var key = RateLimitKey.generate(configurable, subject);
        final var entries = redisTemplate.opsForZSet().range(key, 0, -1);
        assertNotNull(entries);
        assertEquals(expectedNumberOfRequests, entries.size());
    }

    @Test
    void givenSomeAPreSavedRequest_whenCallRemoveRequest_shouldRemoveRequest() {
        // given
        final var subject = EmailAddress.of("test@test.com");
        final var configurable = new NotificationType("Marketing", ChronoUnit.DAYS, 1L, 10L);
        final var anId = NotificationID.unique();
        final var key = RateLimitKey.generate(configurable, subject);

        rateLimitGateway.saveRequest(anId, subject, configurable);
        var entries = redisTemplate.opsForZSet().range(key, 0, -1);
        assertEquals(1, entries.size());

        // when
        rateLimitGateway.removeRequest(anId, subject, configurable);

        // then

        entries = redisTemplate.opsForZSet().range(key, 0, -1);
        assertNotNull(entries);
        assertEquals(0, entries.size());
    }
}