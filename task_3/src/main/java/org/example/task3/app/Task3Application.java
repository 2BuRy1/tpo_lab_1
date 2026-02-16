package org.example.task3.app;

import org.example.task3.scene.SpaceEjectionScene;

public final class Task3Application {
    private Task3Application() {
    }

    public static void main(String[] args) {
        SpaceEjectionScene scene = SpaceEjectionScene.fromNarrative();
        System.out.println(scene.playOutAsNarrativeText());
    }
}
