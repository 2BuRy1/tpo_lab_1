package org.example.task3.domain.space;

public final class OuterSpace {
    private static final String STREWN_STARS_NARRATIVE = "Черная пустота усеялась невероятно яркими звездами.";

    private final boolean open;
    private final boolean blackVoid;
    private final StarField starField;
    private boolean strewnWithStars;

    public OuterSpace(boolean open, boolean blackVoid, StarField starField) {
        if (starField == null) {
            throw new IllegalArgumentException("starField must not be null");
        }
        this.open = open;
        this.blackVoid = blackVoid;
        this.starField = starField;
        this.strewnWithStars = false;
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

    public boolean isStrewnWithStars() {
        return strewnWithStars;
    }

    public void strewWithStars() {
        if (strewnWithStars) {
            throw new IllegalStateException("outer space is already strewn with stars");
        }
        if (!open || !blackVoid) {
            throw new IllegalStateException("stars can only be strewn in open black void");
        }
        this.strewnWithStars = true;
    }

    public String strewnWithStarsNarrative() {
        if (!strewnWithStars) {
            throw new IllegalStateException("outer space is not strewn with stars yet");
        }
        return STREWN_STARS_NARRATIVE;
    }
}
