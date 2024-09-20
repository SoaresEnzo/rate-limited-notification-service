package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.Identifier;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;
import dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider.RateLimitGateway;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Named
public class RateLimitGatewayImpl implements RateLimitGateway {
    private final RedisTemplate<String, RateLimitStoreEntry> redisTemplate;

    @Inject
    public RateLimitGatewayImpl(
            final RedisTemplate<String, RateLimitStoreEntry> redisTemplate
    ) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
    }

    @Override
    public Long getAmountOfRequestsInTheLastPeriodForRecipient(RateLimitSubject subject, RateLimitConfigurable configurable) {
        final var key = STR."RateLimitStoreEntry: \{subject.getSubject()}";
        return Objects
                .requireNonNull(this.redisTemplate
                        .opsForZSet()
                        .rangeByScore(
                                key,
                                Instant
                                        .now()
                                        .minus(configurable.getTimeLimit(),
                                                configurable.getTimePeriod()
                                        ).toEpochMilli(),
                                Instant.now().toEpochMilli()
                        )
                )
                .stream()
                .filter(entry -> entry.getKey().equals(configurable.getKey()))
                .count();

    }

    @Override
    public void saveRequest(Identifier id, RateLimitSubject subject, RateLimitConfigurable configurable) {
        final var entry = RateLimitStoreEntry.newEntry(
                id.getValue(),
                subject,
                configurable
        );
        final var key = STR."RateLimitStoreEntry: \{subject.getSubject()}";
        this.redisTemplate.opsForZSet().add(key, entry, Instant.now().toEpochMilli());
        this.redisTemplate.expire(key, configurable.getTimeLimit(), TimeUnit.of(configurable.getTimePeriod()));
    }
}
