package org.example.task3.domain.space;

public final class OuterSpace {
    private final boolean open;
    private final boolean blackVoid;
    private final StarField starField;

    public OuterSpace(boolean open, boolean blackVoid, StarField starField) {
        if (starField == null) {
            throw new IllegalArgumentException("starField must not be null");
        }
        this.open = open;
        this.blackVoid = blackVoid;
        this.starField = starField;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isBlackVoid() {
        return blackVoid;
    }

    public StarField getStarField() {
        return starField;
    }
}
