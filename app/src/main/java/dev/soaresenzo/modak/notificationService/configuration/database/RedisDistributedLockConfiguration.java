package dev.soaresenzo.modak.notificationService.configuration.database;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.ExpirableLockRegistry;

@Configuration
public class RedisDistributedLockConfiguration {

    private static final String LOCK_REGISTRY_REDIS_KEY = "MICROSERVICE";

    @Bean()
    public ExpirableLockRegistry lockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, LOCK_REGISTRY_REDIS_KEY, 5000);
    }
}