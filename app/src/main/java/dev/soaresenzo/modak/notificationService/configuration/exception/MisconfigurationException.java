package dev.soaresenzo.modak.notificationService.configuration.exception;

public class MisconfigurationException extends RuntimeException {
    public MisconfigurationException(String message) {
      super(message, null, true, false);
    }
}
