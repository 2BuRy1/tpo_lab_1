package org.example.task3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceEjectionSceneTest {

    @Test
    void narrativeSceneStartsBeforeEvents() {
        SpaceEjectionScene scene = SpaceEjectionScene.fromNarrative();

        assertEquals(EngineState.SILENT, scene.getEngine().getState());
        assertEquals(AirSoundState.THIN_WHISTLE, scene.getAirFlow().getSoundState());
        assertTrue(scene.getOuterSpace().isOpen());
        assertTrue(scene.getOuterSpace().isBlackVoid());
        assertEquals(Brightness.INCREDIBLY_BRIGHT, scene.getOuterSpace().getStarField().brightness());
        assertEquals(EjectionStyle.CONFETTI_FROM_POPPER, scene.getEjectionEvent().getStyle());
        assertEquals(List.of("Форд", "Артур"), scene.getEjectionEvent().getCrew().stream().map(CrewMember::getName).toList());
    }

    @Test
    void playOutMovesSceneToFinalNarrativeState() {
        SpaceEjectionScene scene = SpaceEjectionScene.fromNarrative();

        scene.playOut();

        assertTrue(scene.getEngine().isBuzzing());
        assertEquals(AirSoundState.ROAR, scene.getAirFlow().getSoundState());
        assertTrue(scene.getEjectionEvent().getCrew().stream().allMatch(CrewMember::isInOpenSpace));
    }

    @Test
    void playOutAsNarrativeTextReturnsScenarioAndChangesState() {
        SpaceEjectionScene scene = SpaceEjectionScene.fromNarrative();

        String narrative = scene.playOutAsNarrativeText();

        assertTrue(narrative.contains("Зажужжал мотор."));
        assertTrue(narrative.contains("Тоненький свист перерос в рев воздуха"));
        assertTrue(narrative.contains("Форд и Артур вылетели в открытый космос, как конфетти из хлопушки."));
        assertTrue(scene.getEngine().isBuzzing());
        assertEquals(AirSoundState.ROAR, scene.getAirFlow().getSoundState());
        assertTrue(scene.getEjectionEvent().getCrew().stream().allMatch(CrewMember::isInOpenSpace));
    }

    @Test
    void constructorRejectsNullAggregateDependencies() {
        Engine engine = new Engine();
        AirFlow airFlow = new AirFlow();
        OuterSpace outerSpace = new OuterSpace(true, true, new StarField(10, Brightness.BRIGHT));
        EjectionEvent ejectionEvent = new EjectionEvent(List.of(new CrewMember("Форд")), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER);

        assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(null, airFlow, outerSpace, ejectionEvent));
        assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, null, outerSpace, ejectionEvent));
        assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, airFlow, null, ejectionEvent));
        assertThrows(IllegalArgumentException.class, () -> new SpaceEjectionScene(engine, airFlow, outerSpace, null));
    }
}
