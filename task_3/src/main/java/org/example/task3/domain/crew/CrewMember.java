package org.example.task3.domain.crew;

import java.util.Objects;

public final class CrewMember {
    private final String name;
    private Location location;

    public CrewMember(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.location = Location.INSIDE_CRAFT;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isInOpenSpace() {
        return location == Location.OPEN_SPACE;
    }

    public void ejectToOpenSpace() {
        this.location = Location.OPEN_SPACE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrewMember that)) {
            return false;
        }
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
