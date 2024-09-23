package dev.soaresenzo.modak.notificationService.configuration.parameters;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soaresenzo.modak.notificationService.configuration.exception.MisconfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class NotificationConfigurationImpl implements NotificationConfiguration {
    @Value("${notification.properties}")
    private String properties;

    private Map<String, NotificationProperties.RateLimitConfig> propertiesMap = new HashMap<>();

    private void setup() {
        // Parse the JSON into the properties map
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            propertiesMap = objectMapper
                    .readValue(
                            properties,
                            objectMapper
                                    .getTypeFactory()
                                    .constructMapType(Map.class, String.class, NotificationProperties.RateLimitConfig.class)
                    );
        } catch (IOException e) {
            throw new MisconfigurationException("Invalid JSON at properties key");
        }
    }

    public NotificationProperties.RateLimitConfig getRateLimitConfig(String key) {
        this.setup();
        return propertiesMap.get(key);
    }

    @Override
    public Set<String> getAvailableTypes() {
        this.setup();
        return this.propertiesMap.keySet();
    }

    @Override
    public String toString() {
        return "NotificationConfig{" +
                ", propertiesMap=" + propertiesMap +
                '}';
    }
}