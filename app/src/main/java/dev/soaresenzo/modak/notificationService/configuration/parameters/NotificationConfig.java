package dev.soaresenzo.modak.notificationService.configuration.parameters;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soaresenzo.modak.notificationService.configuration.exception.MisconfigurationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class NotificationConfig {
    @Value("${properties}")
    private String properties;  // This will hold the JSON string
    private Map<String, NotificationProperties.RateLimitConfig> propertiesMap = new HashMap<>();

    @PostConstruct
    public void init() {
        // Parse the JSON into the properties map
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            System.out.println("Raw JSON: " + properties);
            propertiesMap = objectMapper.readValue(properties, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, NotificationProperties.RateLimitConfig.class));
        } catch (IOException e) {
            throw new MisconfigurationException("Invalid JSON at properties key");
        }
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Map<String, NotificationProperties.RateLimitConfig> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, NotificationProperties.RateLimitConfig> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public NotificationProperties.RateLimitConfig getRateLimitConfig(String key) {
        return propertiesMap.get(key);
    }

    @Override
    public String toString() {
        return "NotificationConfig{" +
                "properties='" + properties + '\'' +
                ", propertiesMap=" + propertiesMap +
                '}';
    }
}