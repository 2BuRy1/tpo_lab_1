package org.example.task3.scene.engine;

public final class Engine {
    private EngineState state;

    public Engine() {
        this.state = EngineState.SILENT;
    }

    public EngineState getState() {
        return state;
    }

    public boolean isBuzzing() {
        return state == EngineState.BUZZING;
    }

    public void buzz() {
        if (state == EngineState.BUZZING) {
            throw new IllegalStateException("engine is already buzzing");
        }
        this.state = EngineState.BUZZING;
    }
}
