package org.example.task3.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.crew.Location;
import org.example.task3.domain.space.Brightness;
import org.example.task3.domain.space.OuterSpace;
import org.example.task3.domain.space.StarField;
import org.example.task3.scene.air.AirFlow;
import org.example.task3.scene.air.AirSoundState;
import org.example.task3.scene.engine.Engine;
import org.example.task3.scene.engine.EngineState;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainValidationTest {
    private CrewMember ford;
    private StarField brightField;
    private Engine engine;
    private AirFlow airFlow;

    @BeforeEach
    void setUp() {
        ford = new CrewMember("Форд");
        brightField = new StarField(42, Brightness.INCREDIBLY_BRIGHT);
        engine = new Engine();
        airFlow = new AirFlow();
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

    @Test
    void engineStateTransitionsFromSilentToBuzzingOnlyOnce() {
        engine.buzz();

        assertAll(
            () -> assertEquals(EngineState.BUZZING, engine.getState()),
            () -> assertThrows(IllegalStateException.class, engine::buzz)
        );
    }

    @Test
    void airFlowTransitionsFromWhistleToRoarOnlyOnce() {
        airFlow.intensifyToRoar();

        assertAll(
            () -> assertEquals(AirSoundState.ROAR, airFlow.getSoundState()),
            () -> assertThrows(IllegalStateException.class, airFlow::intensifyToRoar)
        );
    }

    @Test
    void crewMemberTransitionsFromInsideCraftToOpenSpaceOnlyOnce() {
        ford.ejectToOpenSpace();

        assertAll(
            () -> assertEquals(Location.OPEN_SPACE, ford.getLocation()),
            () -> assertThrows(IllegalStateException.class, ford::ejectToOpenSpace)
        );
    }
}
