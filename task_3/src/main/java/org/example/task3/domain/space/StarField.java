package org.example.task3.domain.space;

public record StarField(int glowingPointCount, Brightness brightness) {

    public StarField {
        if (glowingPointCount <= 0) {
            throw new IllegalArgumentException("glowingPointCount must be greater than 0");
        }
        if (brightness == null) {
            throw new IllegalArgumentException("brightness must not be null");
        }
    }
}
