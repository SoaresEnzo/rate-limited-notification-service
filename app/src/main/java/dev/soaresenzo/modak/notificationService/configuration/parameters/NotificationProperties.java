package dev.soaresenzo.modak.notificationService.configuration.parameters;

import java.util.Map;

public class NotificationProperties {
    private Map<String, RateLimitConfig> properties;

    public Map<String, RateLimitConfig> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, RateLimitConfig> properties) {
        this.properties = properties;
    }

    public static class RateLimitConfig {
        private int rate;
        private int limit;
        private String period;

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period.toUpperCase();
        }

        @Override
        public String toString() {
            return "RateLimitConfig{" +
                    "rate=" + rate +
                    ", limit=" + limit +
                    ", period='" + period + '\'' +
                    '}';
        }
    }
}