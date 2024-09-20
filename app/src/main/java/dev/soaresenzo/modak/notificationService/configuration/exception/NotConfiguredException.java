package dev.soaresenzo.modak.notificationService.configuration.exception;

public class NotConfiguredException extends RuntimeException {
    public NotConfiguredException(String message) {
        super(message, null, true, false);
    }
}
