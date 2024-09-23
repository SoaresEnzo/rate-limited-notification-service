package dev.soaresenzo.modak.notificationService.configuration.parameters;

import java.util.List;
import java.util.Set;

public interface NotificationConfiguration {

    NotificationProperties.RateLimitConfig getRateLimitConfig(String key);

    Set<String> getAvailableTypes();
}
