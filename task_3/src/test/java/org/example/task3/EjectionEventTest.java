package org.example.task3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EjectionEventTest {

    @Test
    void executeMovesAllCrewMembersIntoOpenSpace() {
        CrewMember ford = new CrewMember("Форд");
        CrewMember arthur = new CrewMember("Артур");
        OuterSpace outerSpace = new OuterSpace(true, true, new StarField(50, Brightness.BRIGHT));
        EjectionEvent event = new EjectionEvent(List.of(ford, arthur), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER);

        event.execute();

        assertTrue(ford.isInOpenSpace());
        assertTrue(arthur.isInOpenSpace());
    }

    @Test
    void getCrewReturnsImmutableSnapshot() {
        CrewMember ford = new CrewMember("Форд");
        OuterSpace outerSpace = new OuterSpace(true, true, new StarField(10, Brightness.BRIGHT));
        EjectionEvent event = new EjectionEvent(List.of(ford), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER);

        List<CrewMember> crewView = event.getCrew();

        assertThrows(UnsupportedOperationException.class, () -> crewView.add(new CrewMember("Артур")));
        assertEquals(1, event.getCrew().size());
    }

    @Test
    void constructorValidatesInput() {
        CrewMember ford = new CrewMember("Форд");
        OuterSpace outerSpace = new OuterSpace(true, true, new StarField(10, Brightness.BRIGHT));

        assertThrows(IllegalArgumentException.class, () -> new EjectionEvent(null, outerSpace, EjectionStyle.CONFETTI_FROM_POPPER));
        assertThrows(IllegalArgumentException.class, () -> new EjectionEvent(List.of(), outerSpace, EjectionStyle.CONFETTI_FROM_POPPER));

        List<CrewMember> withNull = new ArrayList<>();
        withNull.add(ford);
        withNull.add(null);
        assertThrows(IllegalArgumentException.class, () -> new EjectionEvent(withNull, outerSpace, EjectionStyle.CONFETTI_FROM_POPPER));

        assertThrows(IllegalArgumentException.class, () -> new EjectionEvent(List.of(ford), null, EjectionStyle.CONFETTI_FROM_POPPER));
        assertThrows(IllegalArgumentException.class, () -> new EjectionEvent(List.of(ford), outerSpace, null));
    }
}
