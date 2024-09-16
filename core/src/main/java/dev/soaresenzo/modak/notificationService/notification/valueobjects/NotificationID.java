package dev.soaresenzo.modak.notificationService.notification.valueobjects;

import dev.soaresenzo.modak.notificationService.Identifier;

import java.util.Objects;
import java.util.UUID;

public class NotificationID extends Identifier {
    private final String value;

    private NotificationID(String value) {
        this.value = value;
    }

    public static NotificationID from(final String anId) {
        return new NotificationID(anId);
    }

    public static NotificationID unique() {
        return NotificationID.from(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return this.value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationID that = (NotificationID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
