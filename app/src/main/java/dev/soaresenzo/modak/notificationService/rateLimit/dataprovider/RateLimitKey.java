package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;

public class RateLimitKey {

    public static String generate(RateLimitConfigurable configurable, RateLimitSubject subject) {
        return STR."\{configurable.getKey()}RateLimitStoreEntry-\{subject.getSubject()}";
    }

    public static String generate(String key, String subject) {
        return STR."\{key}RateLimitStoreEntry-\{subject}";
    }
}
