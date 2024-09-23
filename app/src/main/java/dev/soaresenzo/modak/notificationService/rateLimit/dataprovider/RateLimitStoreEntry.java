package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;
import org.springframework.data.annotation.Id;


import java.util.Objects;


public class RateLimitStoreEntry {
    @Id
    private String id;
    private String key;
    private String subject;


    private RateLimitStoreEntry(String id, String key, String subject) {
        this.id = id;
        this.key = key;
        this.subject = subject;
    }

    public RateLimitStoreEntry() {
    }

    public static RateLimitStoreEntry newEntry(String id, RateLimitSubject subject, RateLimitConfigurable configurable) {
        return new RateLimitStoreEntry(id, configurable.getKey(), subject.getSubject());
    }

    public static RateLimitStoreEntry newEntry(String id, String subject, String key) {
        return new RateLimitStoreEntry(id, key, subject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "RateLimitStoreEntry{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateLimitStoreEntry that = (RateLimitStoreEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(key, that.key) && Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, subject);
    }
}
