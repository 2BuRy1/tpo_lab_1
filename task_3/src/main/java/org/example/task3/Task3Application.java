package org.example.task3;

public final class Task3Application {
    private Task3Application() {
    }

    public static void main(String[] args) {
        SpaceEjectionScene scene = SpaceEjectionScene.fromNarrative();
        System.out.println(scene.playOutAsNarrativeText());
    }

}
