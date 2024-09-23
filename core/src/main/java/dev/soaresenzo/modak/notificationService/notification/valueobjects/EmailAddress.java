package dev.soaresenzo.modak.notificationService.notification.valueobjects;

import dev.soaresenzo.modak.notificationService.notification.exceptions.EmailValidationException;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;

import java.util.Objects;
import java.util.regex.Pattern;

public class EmailAddress implements RateLimitSubject {
    private final String value;

    private EmailAddress(final String value) {
        this.value = value;
    }

    public static EmailAddress of(final String email) {
         EmailAddress.validate(email);
         return new EmailAddress(email);
    }

    public static void validate(final String email) {
        if (Objects.isNull(email)) {
            throw new EmailValidationException("The email must not be null.");
        }

        if(email.length() > 254) {
            throw new EmailValidationException("The email length must be 254 or lesser.");
        }

        final var emailPattern = Pattern.compile("^[\\w\\.-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,}$");
        if (!emailPattern.matcher(email).find()) {
            throw new EmailValidationException("The email does not meet the required format.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getSubject() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAddress that = (EmailAddress) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
