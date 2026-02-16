package org.example.task3.scene;

import java.util.List;
import java.util.stream.Collectors;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.event.EjectionEvent;
import org.example.task3.domain.event.EjectionStyle;
import org.example.task3.domain.space.Brightness;
import org.example.task3.domain.space.OuterSpace;
import org.example.task3.domain.space.StarField;
import org.example.task3.scene.air.AirFlow;
import org.example.task3.scene.engine.Engine;

public final class SpaceEjectionScene {
    private static final int DEFAULT_GLOWING_POINT_COUNT = 1_000;

    private final Engine engine;
    private final AirFlow airFlow;
    private final OuterSpace outerSpace;
    private final EjectionEvent ejectionEvent;

    public SpaceEjectionScene(Engine engine, AirFlow airFlow, OuterSpace outerSpace, EjectionEvent ejectionEvent) {
        if (engine == null) {
            throw new IllegalArgumentException("engine must not be null");
        }
        if (airFlow == null) {
            throw new IllegalArgumentException("airFlow must not be null");
        }
        if (outerSpace == null) {
            throw new IllegalArgumentException("outerSpace must not be null");
        }
        if (ejectionEvent == null) {
            throw new IllegalArgumentException("ejectionEvent must not be null");
        }
        this.engine = engine;
        this.airFlow = airFlow;
        this.outerSpace = outerSpace;
        this.ejectionEvent = ejectionEvent;
    }

    public static SpaceEjectionScene fromNarrative() {
        Engine engine = new Engine();
        AirFlow airFlow = new AirFlow();
        OuterSpace outerSpace = new OuterSpace(
            true,
            true,
            new StarField(DEFAULT_GLOWING_POINT_COUNT, Brightness.INCREDIBLY_BRIGHT)
        );

        CrewMember ford = new CrewMember("Форд");
        CrewMember arthur = new CrewMember("Артур");
        EjectionEvent ejectionEvent = new EjectionEvent(
            List.of(ford, arthur),
            outerSpace,
            EjectionStyle.CONFETTI_FROM_POPPER
        );
        return new SpaceEjectionScene(engine, airFlow, outerSpace, ejectionEvent);
    }

    public Engine getEngine() {
        return engine;
    }

    public AirFlow getAirFlow() {
        return airFlow;
    }

    public OuterSpace getOuterSpace() {
        return outerSpace;
    }

    public EjectionEvent getEjectionEvent() {
        return ejectionEvent;
    }

    public void playOut() {
        engine.buzz();
        airFlow.intensifyToRoar();
        ejectionEvent.execute();
    }

    public String playOutAsNarrativeText() {
        String lineSeparator = System.lineSeparator();
        StringBuilder narrative = new StringBuilder();

        engine.buzz();
        narrative.append("Зажужжал мотор.").append(lineSeparator);

        airFlow.intensifyToRoar();
        narrative.append(
            "Тоненький свист перерос в рев воздуха, вырывающегося в черную пустоту, усеянную невероятно яркими светящимися точками."
        ).append(lineSeparator);

        ejectionEvent.execute();
        String crewNames = ejectionEvent.getCrew().stream().map(CrewMember::getName).collect(Collectors.joining(" и "));
        narrative
            .append(crewNames)
            .append(" вылетели в открытый космос, как конфетти из хлопушки.");

        return narrative.toString();
    }
}
