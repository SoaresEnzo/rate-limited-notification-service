package dev.soaresenzo.modak.notificationService.rateLimiter.exceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message, null);
    }
}
