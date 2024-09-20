package dev.soaresenzo.modak.notificationService.rateLimit.dataprovider;

import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Objects;


public class RateLimitStoreEntry {
    @Id
    private String id;
    private String key;
    private String subject;
    @TimeToLive
    private long ttl = 60 * 60 * 24 * 30;

    private RateLimitStoreEntry(String id, String key, String subject) {
        this.id = id;
        this.key = key;
        this.subject = subject;
    }

    public RateLimitStoreEntry() {
    }

    @PersistenceCreator
    public static RateLimitStoreEntry of(String id, String key, String subject) {
        return new RateLimitStoreEntry(id, key, subject);
    }

    public static RateLimitStoreEntry newEntry(String id, RateLimitSubject subject, RateLimitConfigurable configurable) {
        return new RateLimitStoreEntry(id, configurable.getKey(), subject.getSubject());
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

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "RateLimitStoreEntry{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", subject='" + subject + '\'' +
                ", ttl=" + ttl +
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
