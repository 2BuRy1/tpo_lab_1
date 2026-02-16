package org.example.task3.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.crew.Location;
import org.example.task3.domain.space.Brightness;
import org.example.task3.domain.space.OuterSpace;
import org.example.task3.domain.space.StarField;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainValidationTest {
    private CrewMember ford;
    private StarField brightField;

    @BeforeEach
    void setUp() {
        ford = new CrewMember("Форд");
        brightField = new StarField(42, Brightness.INCREDIBLY_BRIGHT);
    }

    @Test
    void crewMemberStartsInsideCraftAndValidatesName() {
        assertAll(
            () -> assertEquals(Location.INSIDE_CRAFT, ford.getLocation()),
            () -> assertThrows(IllegalArgumentException.class, () -> new CrewMember(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> new CrewMember(" "))
        );
    }

    @Test
    void starFieldValidatesValues() {
        assertAll(
            () -> assertEquals(42, brightField.glowingPointCount()),
            () -> assertEquals(Brightness.INCREDIBLY_BRIGHT, brightField.brightness()),
            () -> assertThrows(IllegalArgumentException.class, () -> new StarField(0, Brightness.BRIGHT)),
            () -> assertThrows(IllegalArgumentException.class, () -> new StarField(-1, Brightness.BRIGHT)),
            () -> assertThrows(IllegalArgumentException.class, () -> new StarField(1, null))
        );
    }

    @Test
    void outerSpaceRequiresStarField() {
        assertThrows(IllegalArgumentException.class, () -> new OuterSpace(true, true, null));
    }
}
