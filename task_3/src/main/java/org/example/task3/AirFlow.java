package org.example.task3;

public final class AirFlow {
    private AirSoundState soundState;

    public AirFlow() {
        this.soundState = AirSoundState.THIN_WHISTLE;
    }

    public AirSoundState getSoundState() {
        return soundState;
    }

    public void intensifyToRoar() {
        this.soundState = AirSoundState.ROAR;
    }
}
