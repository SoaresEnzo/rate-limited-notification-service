package dev.soaresenzo.modak.notificationService.configuration.parameters;

import java.util.Map;

public record NotificationProperties (
        Map<String, RateLimitConfig> properties
) {
    public record  RateLimitConfig(
            int rate,
            int limit,
            String period
    ) {

    }
}