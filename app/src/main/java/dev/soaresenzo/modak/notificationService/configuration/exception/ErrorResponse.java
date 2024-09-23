package dev.soaresenzo.modak.notificationService.configuration.exception;

import java.time.Instant;

public record ErrorResponse(
        String message,
        Instant timestamp
) {
}
