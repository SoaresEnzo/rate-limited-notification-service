package dev.soaresenzo.modak.notificationService;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected ID id;

    public abstract void validate();

    public ID getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
