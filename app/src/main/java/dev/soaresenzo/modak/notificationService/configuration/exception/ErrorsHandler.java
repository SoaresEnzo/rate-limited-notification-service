package dev.soaresenzo.modak.notificationService.configuration.exception;

import dev.soaresenzo.modak.notificationService.notification.exceptions.EmailValidationException;
import dev.soaresenzo.modak.notificationService.notification.exceptions.NotificationException;
import dev.soaresenzo.modak.notificationService.rateLimiter.exceptions.RateLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ErrorsHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorsHandler.class);

    @ExceptionHandler(MisconfigurationException.class)
    public ResponseEntity<ErrorResponse> handleMisconfigurationException(MisconfigurationException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(500).body(new ErrorResponse(exception.getMessage(), Instant.now()));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(429).body(new ErrorResponse(exception.getMessage(), Instant.now()));
    }

    @ExceptionHandler(NotConfiguredException.class)
    public ResponseEntity<ErrorResponse> handleNotConfiguredException(NotConfiguredException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(), Instant.now()));
    }

    @ExceptionHandler({EmailValidationException.class, NotificationException.class})
    public ResponseEntity<ErrorResponse> handleEmailValidationException(EmailValidationException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(), Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(500).body(new ErrorResponse(exception.getMessage(), Instant.now()));
    }
}
