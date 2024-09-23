package dev.soaresenzo.modak.notificationService.configuration.database;

import dev.soaresenzo.modak.notificationService.rateLimit.dataprovider.RateLimitStoreEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@EnableConfigurationProperties
public class RedisConfig {
    @Value("${ratelimit.store.host}")
    private String host;
    @Value("${ratelimit.store.port}")
    private String port;
    @Value("${ratelimit.store.password}")
    private String password;
    @Value("${ratelimit.store.username}")
    private String username;

    //connection config
    @Bean
    public RedisConnectionFactory connection() {

        final var redisConfig = new RedisStandaloneConfiguration(host);
        redisConfig.setPassword(password);
        redisConfig.setUsername(username);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, RateLimitStoreEntry> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RateLimitStoreEntry> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    public RedisConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
