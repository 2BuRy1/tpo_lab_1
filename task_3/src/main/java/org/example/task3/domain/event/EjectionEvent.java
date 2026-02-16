package org.example.task3.domain.event;

import java.util.List;

import org.example.task3.domain.crew.CrewMember;
import org.example.task3.domain.space.OuterSpace;

public final class EjectionEvent {
    private final List<CrewMember> crew;
    private final OuterSpace destination;
    private final EjectionStyle style;

    public EjectionEvent(List<CrewMember> crew, OuterSpace destination, EjectionStyle style) {
        if (crew == null || crew.isEmpty()) {
            throw new IllegalArgumentException("crew must not be empty");
        }
        if (crew.stream().anyMatch(member -> member == null)) {
            throw new IllegalArgumentException("crew must not contain nulls");
        }
        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }
        if (style == null) {
            throw new IllegalArgumentException("style must not be null");
        }

        this.crew = List.copyOf(crew);
        this.destination = destination;
        this.style = style;
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public OuterSpace getDestination() {
        return destination;
    }

    public EjectionStyle getStyle() {
        return style;
    }

    public void execute() {
        for (CrewMember crewMember : crew) {
            crewMember.ejectToOpenSpace();
        }
    }
}
