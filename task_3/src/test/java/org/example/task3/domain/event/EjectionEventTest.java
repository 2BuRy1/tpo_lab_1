package org.example.task3.domain.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.space.Brightness;
import org.example.task3.domain.space.OuterSpace;
import org.example.task3.domain.space.StarField;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EjectionEventTest {
    private CrewMember ford;
    private CrewMember arthur;
    private OuterSpace outerSpace;
    private EjectionEvent event;

    @BeforeEach
    void setUp() {
        ford = new CrewMember("Форд");
        arthur = new CrewMember("Артур");
        outerSpace = new OuterSpace(true, true, new StarField(50, Brightness.BRIGHT));
        event = new EjectionEvent(List.of(ford, arthur), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER);
    }

    @Test
    void executeMovesAllCrewMembersIntoOpenSpace() {
        event.execute();

        assertAll(
            () -> assertTrue(ford.isInOpenSpace()),
            () -> assertTrue(arthur.isInOpenSpace())
        );
    }

    @Test
    void getCrewReturnsImmutableSnapshot() {
        List<CrewMember> crewView = event.getCrew();

        assertAll(
            () -> assertThrows(UnsupportedOperationException.class, () -> crewView.add(new CrewMember("Артур"))),
            () -> assertEquals(2, event.getCrew().size())
        );
    }

    @Test
    void constructorValidatesInput() {
        List<CrewMember> withNull = new ArrayList<>();
        withNull.add(ford);
        withNull.add(null);

        assertAll(
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new EjectionEvent(null, outerSpace, EjectionStyle.CONFETTI_FROM_POPPER)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new EjectionEvent(List.of(), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new EjectionEvent(withNull, outerSpace, EjectionStyle.CONFETTI_FROM_POPPER)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new EjectionEvent(List.of(ford), null, EjectionStyle.CONFETTI_FROM_POPPER)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> new EjectionEvent(List.of(ford), outerSpace, null)
            )
        );
    }
}
