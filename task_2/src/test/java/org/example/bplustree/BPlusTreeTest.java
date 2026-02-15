package org.example.bplustree;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
class BPlusTreeTest {

    @Test
    void defaultConfigurationUsesRequiredMaxKeys() {
        BPlusTree tree = new BPlusTree();
        assertEquals(7, tree.getMaxKeys());
    }

    @Test
    void insertAndSearchWorkForOrderedInput() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 200; key += 10) {
            tree.insert(key);
        }

        for (int key = 10; key <= 200; key += 10) {
            assertTrue(tree.contains(key));
        }

        List<Integer> expectedOrder = IntStream.rangeClosed(1, 20)
            .map(i -> i * 10)
            .boxed()
            .collect(Collectors.toList());

        assertEquals(expectedOrder, tree.keysInOrder());
    }

    @Test
    void rejectsInvalidMaxKeys() {
        assertThrows(IllegalArgumentException.class, () -> new BPlusTree(2));
    }

    @Test
    void traceMatchesReferenceForDatasetWithoutSplits() {
        BPlusTree tree = new BPlusTree();

        List<String> actualTrace = tree.insertAllWithTrace(List.of(10, 20, 30));

        List<String> expectedTrace = List.of(
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT"
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void traceMatchesReferenceForDatasetWithLeafSplitAndNewRoot() {
        BPlusTree tree = new BPlusTree();

        List<String> actualTrace = tree.insertAllWithTrace(List.of(10, 20, 30, 40, 50, 60, 70, 80));

        List<String> expectedTrace = List.of(
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "END_INSERT",
            "START_INSERT", "ROOT_IS_LEAF", "LEAF_FOUND", "LEAF_INSERT", "LEAF_OVERFLOW", "SPLIT_LEAF", "NEW_ROOT", "END_INSERT"
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void traceMatchesReferenceForDatasetWithInternalSplit() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 350; key += 10) {
            tree.insert(key);
        }

        List<String> actualTrace = tree.insertWithTrace(360);

        List<String> expectedTrace = List.of(
            "START_INSERT",
            "DESCEND_INTERNAL",
            "LEAF_FOUND",
            "LEAF_INSERT",
            "LEAF_OVERFLOW",
            "SPLIT_LEAF",
            "INSERT_IN_PARENT",
            "INTERNAL_OVERFLOW",
            "SPLIT_INTERNAL",
            "NEW_ROOT",
            "END_INSERT"
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void duplicateInsertionProducesReferenceTrace() {
        BPlusTree tree = new BPlusTree();
        tree.insert(42);

        List<String> actualTrace = tree.insertWithTrace(42);

        List<String> expectedTrace = List.of(
            "START_INSERT",
            "ROOT_IS_LEAF",
            "LEAF_FOUND",
            "DUPLICATE_KEY",
            "END_INSERT"
        );

        assertEquals(expectedTrace, actualTrace);
    }
}
