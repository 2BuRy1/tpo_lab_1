package org.example.task3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainValidationTest {

    @Test
    void crewMemberStartsInsideCraftAndValidatesName() {
        CrewMember ford = new CrewMember("Форд");
        assertEquals(Location.INSIDE_CRAFT, ford.getLocation());

        assertThrows(IllegalArgumentException.class, () -> new CrewMember(null));
        assertThrows(IllegalArgumentException.class, () -> new CrewMember(" "));
    }

    @Test
    void starFieldValidatesValues() {
        StarField field = new StarField(42, Brightness.INCREDIBLY_BRIGHT);
        assertEquals(42, field.glowingPointCount());
        assertEquals(Brightness.INCREDIBLY_BRIGHT, field.brightness());

        assertThrows(IllegalArgumentException.class, () -> new StarField(0, Brightness.BRIGHT));
        assertThrows(IllegalArgumentException.class, () -> new StarField(-1, Brightness.BRIGHT));
        assertThrows(IllegalArgumentException.class, () -> new StarField(1, null));
    }

    @Test
    void outerSpaceRequiresStarField() {
        assertThrows(IllegalArgumentException.class, () -> new OuterSpace(true, true, null));
    }
}
