package dev.soaresenzo.modak.notificationService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class TestRedisConfiguration {

    private RedisServer redisServer;

    public TestRedisConfiguration() throws IOException {
        System.out.println("Starting Redis Server");
        this.redisServer = new RedisServer(6379);
    }

    @PostConstruct
    public void postConstruct() {
        try {
            redisServer.start();
        } catch (Exception e) {
            System.out.println("Error starting Redis Server");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        System.out.println("Stopping Redis Server");
        redisServer.stop();
    }
}
