package org.example.task3.scene.air;

import java.util.List;

import org.example.task3.domain.space.OuterSpace;

public final class AirFlow {
    private static final String ROAR_NARRATIVE = "Тоненький свист перерос в рев воздуха.";
    private static final String BURST_NARRATIVE = "Рев воздуха вырвался в черную пустоту.";

    private AirSoundState soundState;
    private boolean burstIntoBlackVoid;

    public AirFlow() {
        this.soundState = AirSoundState.THIN_WHISTLE;
        this.burstIntoBlackVoid = false;
    }

    public AirSoundState getSoundState() {
        return soundState;
    }

    public boolean isBurstIntoBlackVoid() {
        return burstIntoBlackVoid;
    }

    public void intensifyToRoar() {
        if (soundState == AirSoundState.ROAR) {
            throw new IllegalStateException("air flow is already roaring");
        }
        this.soundState = AirSoundState.ROAR;
    }

    public void burstIntoBlackVoid(OuterSpace destination) {
        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }
        if (soundState != AirSoundState.ROAR) {
            throw new IllegalStateException("air flow must roar before bursting into black void");
        }
        if (burstIntoBlackVoid) {
            throw new IllegalStateException("air flow has already burst into black void");
        }
        destination.strewWithStars();
        this.burstIntoBlackVoid = true;
    }

    public String intensifyToRoarWithNarrative() {
        intensifyToRoar();
        return ROAR_NARRATIVE;
    }

    public List<String> burstIntoBlackVoidWithNarrative(OuterSpace destination) {
        burstIntoBlackVoid(destination);
        return List.of(BURST_NARRATIVE, destination.strewnWithStarsNarrative());
    }
}
