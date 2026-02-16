package org.example.task3.scene;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.event.EjectionEvent;
import org.example.task3.domain.event.EjectionEventState;
import org.example.task3.domain.event.EjectionStyle;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceEjectionSceneTest {
    private static String expectedNarrative;

    private SpaceEjectionScene scene;
    private Engine engine;
    private AirFlow airFlow;
    private OuterSpace outerSpace;
    private EjectionEvent ejectionEvent;

    @BeforeAll
    static void beforeAll() {
        expectedNarrative = "Зажужжал мотор." + System.lineSeparator()
            + "Тоненький свист перерос в рев воздуха, вырывающегося в черную пустоту, усеянную невероятно яркими светящимися точками."
            + System.lineSeparator()
            + "Форд и Артур вылетели в открытый космос, как конфетти из хлопушки.";
    }

    @BeforeEach
    void setUp() {
        scene = SpaceEjectionScene.fromNarrative();

        engine = new Engine();
        airFlow = new AirFlow();
        outerSpace = new OuterSpace(true, true, new StarField(10, Brightness.BRIGHT));
        ejectionEvent = new EjectionEvent(
            List.of(new CrewMember("Форд"), new CrewMember("Артур")),
            outerSpace,
            EjectionStyle.CONFETTI_FROM_POPPER
        );
    }

    @Test
    void narrativeSceneStartsBeforeEvents() {
        assertAll(
            () -> assertEquals(SceneState.INITIAL, scene.getState()),
            () -> assertEquals(EngineState.SILENT, scene.getEngine().getState()),
            () -> assertEquals(AirSoundState.THIN_WHISTLE, scene.getAirFlow().getSoundState()),
            () -> assertTrue(scene.getOuterSpace().isOpen()),
            () -> assertTrue(scene.getOuterSpace().isBlackVoid()),
            () -> assertEquals(Brightness.INCREDIBLY_BRIGHT, scene.getOuterSpace().getStarField().brightness()),
            () -> assertEquals(EjectionEventState.READY, scene.getEjectionEvent().getState()),
            () -> assertEquals(EjectionStyle.CONFETTI_FROM_POPPER, scene.getEjectionEvent().getStyle()),
            () -> assertEquals(
                List.of("Форд", "Артур"),
                scene.getEjectionEvent().getCrew().stream().map(CrewMember::getName).toList()
            )
        );
    }

    @Test
    void playOutTransitionsSceneFromInitialToCompleted() {
        scene.playOut();

        assertAll(
            () -> assertEquals(SceneState.COMPLETED, scene.getState()),
            () -> assertTrue(scene.getEngine().isBuzzing()),
            () -> assertEquals(AirSoundState.ROAR, scene.getAirFlow().getSoundState()),
            () -> assertEquals(EjectionEventState.EXECUTED, scene.getEjectionEvent().getState()),
            () -> assertTrue(scene.getEjectionEvent().getCrew().stream().allMatch(CrewMember::isInOpenSpace))
        );
    }

    @Test
    void playOutAsNarrativeTextTransitionsStateAndReturnsExpectedText() {
        String narrative = scene.playOutAsNarrativeText();

        assertAll(
            () -> assertEquals(expectedNarrative, narrative),
            () -> assertEquals(SceneState.COMPLETED, scene.getState()),
            () -> assertEquals(EjectionEventState.EXECUTED, scene.getEjectionEvent().getState())
        );
    }

    @Test
    void sceneRejectsRepeatedPlayOutTransitions() {
        scene.playOut();

        assertThrows(IllegalStateException.class, scene::playOut);
    }

    @Test
    void sceneRejectsPlayOutAfterNarrativeGeneration() {
        scene.playOutAsNarrativeText();

        assertThrows(IllegalStateException.class, scene::playOut);
    }

    @Test
    void constructorRejectsNullAggregateDependencies() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(null, airFlow, outerSpace, ejectionEvent)),
            () -> assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, null, outerSpace, ejectionEvent)),
            () -> assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, airFlow, null, ejectionEvent)),
            () -> assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, airFlow, outerSpace, null))
        );
    }
}
