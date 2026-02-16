package org.example.task3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpaceEjectionSceneTest {
    private static String expectedNarrative;

    @Mock
    private Engine engine;

    @Mock
    private AirFlow airFlow;

    @Mock
    private OuterSpace outerSpace;

    @Mock
    private EjectionEvent ejectionEvent;

    @Mock
    private CrewMember ford;

    @Mock
    private CrewMember arthur;

    private SpaceEjectionScene narrativeScene;
    private SpaceEjectionScene mockedScene;
    private Engine realEngine;
    private AirFlow realAirFlow;
    private OuterSpace realOuterSpace;
    private EjectionEvent realEjectionEvent;

    @BeforeAll
    static void beforeAll() {
        expectedNarrative = "Зажужжал мотор." + System.lineSeparator()
            + "Тоненький свист перерос в рев воздуха, вырывающегося в черную пустоту, усеянную невероятно яркими светящимися точками."
            + System.lineSeparator()
            + "Форд и Артур вылетели в открытый космос, как конфетти из хлопушки.";
    }

    @BeforeEach
    void setUp() {
        narrativeScene = SpaceEjectionScene.fromNarrative();
        mockedScene = new SpaceEjectionScene(engine, airFlow, outerSpace, ejectionEvent);

        realEngine = new Engine();
        realAirFlow = new AirFlow();
        realOuterSpace = new OuterSpace(true, true, new StarField(10, Brightness.BRIGHT));
        realEjectionEvent = new EjectionEvent(
            List.of(new CrewMember("Форд")),
            realOuterSpace,
            EjectionStyle.CONFETTI_FROM_POPPER
        );
    }

    @Test
    void narrativeSceneStartsBeforeEvents() {
        assertAll(
            () -> assertEquals(EngineState.SILENT, narrativeScene.getEngine().getState()),
            () -> assertEquals(AirSoundState.THIN_WHISTLE, narrativeScene.getAirFlow().getSoundState()),
            () -> assertTrue(narrativeScene.getOuterSpace().isOpen()),
            () -> assertTrue(narrativeScene.getOuterSpace().isBlackVoid()),
            () -> assertEquals(Brightness.INCREDIBLY_BRIGHT, narrativeScene.getOuterSpace().getStarField().brightness()),
            () -> assertEquals(EjectionStyle.CONFETTI_FROM_POPPER, narrativeScene.getEjectionEvent().getStyle()),
            () -> assertEquals(
                List.of("Форд", "Артур"),
                narrativeScene.getEjectionEvent().getCrew().stream().map(CrewMember::getName).toList()
            )
        );
    }

    @Test
    void playOutCallsSceneModulesInOrder() {
        mockedScene.playOut();

        InOrder inOrder = inOrder(engine, airFlow, ejectionEvent);
        inOrder.verify(engine).buzz();
        inOrder.verify(airFlow).intensifyToRoar();
        inOrder.verify(ejectionEvent).execute();
    }

    @Test
    void playOutAsNarrativeTextUsesMockedCrewNamesAndTriggersModules() {
        when(ejectionEvent.getCrew()).thenReturn(List.of(ford, arthur));
        when(ford.getName()).thenReturn("Форд");
        when(arthur.getName()).thenReturn("Артур");

        String narrative = mockedScene.playOutAsNarrativeText();

        assertAll(
            () -> assertEquals(expectedNarrative, narrative),
            () -> verify(engine).buzz(),
            () -> verify(airFlow).intensifyToRoar(),
            () -> verify(ejectionEvent).execute(),
            () -> verify(ejectionEvent).getCrew()
        );
    }

    @Test
    void constructorRejectsNullAggregateDependencies() {
        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new SpaceEjectionScene(null, realAirFlow, realOuterSpace, realEjectionEvent)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new SpaceEjectionScene(realEngine, null, realOuterSpace, realEjectionEvent)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new SpaceEjectionScene(realEngine, realAirFlow, null, realEjectionEvent)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new SpaceEjectionScene(realEngine, realAirFlow, realOuterSpace, null)
            )
        );
    }
}
