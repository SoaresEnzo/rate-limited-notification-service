package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.Identifier;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;
import dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider.RateLimitGateway;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.support.locks.ExpirableLockRegistry;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Named
public class RateLimitGatewayImpl implements RateLimitGateway {
    private final RedisTemplate<String, RateLimitStoreEntry> redisTemplate;
    private final ExpirableLockRegistry lockRegistry;

    @Inject
    public RateLimitGatewayImpl(
            final RedisTemplate<String, RateLimitStoreEntry> redisTemplate,
            final ExpirableLockRegistry lockRegistry
    ) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
        this.lockRegistry = Objects.requireNonNull(lockRegistry);
    }

    @Override
    public Long getAmountOfRequestsInTheLastPeriodForRecipient(RateLimitSubject subject, RateLimitConfigurable configurable) {
        final var key = RateLimitKey.generate(configurable, subject);
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
        final var key = RateLimitKey.generate(configurable, subject);
        final var lock = this.lockRegistry.obtain(key);
        final var success = lock.tryLock();
        if (!success) {
            throw new RuntimeException("Could not obtain lock");
        }

        try {
            final var entry = RateLimitStoreEntry.newEntry(
                    id.getValue(),
                    subject,
                    configurable
            );

            this.redisTemplate.opsForZSet().add(key, entry, Instant.now().toEpochMilli());
            this.redisTemplate.expire(key, configurable.getTimeLimit(), TimeUnit.of(configurable.getTimePeriod()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeRequest(Identifier id, RateLimitSubject subject, RateLimitConfigurable configurable) {
        final var key = RateLimitKey.generate(configurable, subject);

        final var entry = RateLimitStoreEntry.newEntry(
                id.getValue(),
                subject,
                configurable
        );
        this.redisTemplate.opsForZSet().remove(key, entry);
    }
}