package org.example.task3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class Task3PlaceholderTest {

    @Test
    void descriptionIsNotEmpty() {
        Task3Placeholder placeholder = new Task3Placeholder();
        assertFalse(placeholder.description().isBlank());
    }
}
