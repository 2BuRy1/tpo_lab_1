package org.example.bplustree;

import org.example.bplustree.utils.TraceStates;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.bplustree.utils.TraceStates.*;
import static org.junit.jupiter.api.Assertions.*;

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

        List<TraceStates> actualTrace = tree.insertAllWithTrace(List.of(10, 20, 30));

        List<TraceStates> expectedTrace = List.of(
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void traceMatchesReferenceForDatasetWithLeafSplitAndNewRoot() {
        BPlusTree tree = new BPlusTree();

        List<TraceStates> actualTrace = tree.insertAllWithTrace(List.of(10, 20, 30, 40, 50, 60, 70, 80));

        List<TraceStates> expectedTrace = List.of(
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, END_INSERT,
            START_INSERT, ROOT_IS_LEAF, LEAF_FOUND, LEAF_INSERT, LEAF_OVERFLOW, SPLIT_LEAF, NEW_ROOT, END_INSERT
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void traceMatchesReferenceForDatasetWithInternalSplit() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 350; key += 10) {
            tree.insert(key);
        }

        List<TraceStates> actualTrace = tree.insertWithTrace(360);

        List<TraceStates> expectedTrace = List.of(
            START_INSERT,
            DESCEND_INTERNAL,
            LEAF_FOUND,
            LEAF_INSERT,
            LEAF_OVERFLOW,
            SPLIT_LEAF,
            INSERT_IN_PARENT,
            INTERNAL_OVERFLOW,
            SPLIT_INTERNAL,
            NEW_ROOT,
            END_INSERT
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void duplicateInsertionProducesReferenceTrace() {
        BPlusTree tree = new BPlusTree();
        tree.insert(42);

        List<TraceStates> actualTrace = tree.insertWithTrace(42);

        List<TraceStates> expectedTrace = List.of(
            START_INSERT,
            ROOT_IS_LEAF,
            LEAF_FOUND,
            DUPLICATE_KEY,
            END_INSERT
        );

        assertEquals(expectedTrace, actualTrace);
    }

    @Test
    void containsReturnsFalseForMissingKeys() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 200; key += 10) {
            tree.insert(key);
        }

        assertFalse(tree.contains(5));
        assertFalse(tree.contains(205));
        assertFalse(tree.contains(15));
        assertFalse(tree.contains(55));
    }

    @Test
    void traceContainsInsertInParentWithoutInternalOverflow() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 110; key += 10) {
            tree.insert(key);
        }


        List<TraceStates> trace = tree.insertWithTrace(120);

        assertTrue(trace.contains(DESCEND_INTERNAL));
        assertTrue(trace.contains(LEAF_OVERFLOW));
        assertTrue(trace.contains(SPLIT_LEAF));
        assertTrue(trace.contains(INSERT_IN_PARENT));

        assertFalse(trace.contains(INTERNAL_OVERFLOW), "parent must not overflow in this scenario");
        assertFalse(trace.contains(NEW_ROOT), "new root must not be created in this scenario");
    }

    @Test
    void traceContainsInternalSplitWithoutNewRoot() {
        BPlusTree tree = new BPlusTree();

        for (int key = 10; key <= 550; key += 10) {
            tree.insert(key);
        }

        List<TraceStates> trace = tree.insertWithTrace(560);

        assertTrue(trace.contains(INTERNAL_OVERFLOW), "internal overflow must happen");
        assertTrue(trace.contains(SPLIT_INTERNAL), "internal split must happen");

        assertFalse(trace.contains(NEW_ROOT), "new root must NOT be created for non-root internal split");

        long descents = trace.stream().filter(DESCEND_INTERNAL::equals).count();
        assertTrue(descents >= 2, "expected at least two internal descents (height >= 3)");
    }

    @Test
    void keysInOrderIsSortedAndHasNoDuplicates_afterMixedInsertions() {
        BPlusTree tree = new BPlusTree();

        List<Integer> keys = new ArrayList<>();
        keys.addAll(IntStream.rangeClosed(1, 50).map(i -> i * 10).boxed().collect(Collectors.toList()));
        Collections.shuffle(keys, new Random(1));

        keys.add(100);
        keys.add(200);
        keys.add(300);

        for (int k : keys) {
            tree.insert(k);
        }

        List<Integer> inOrder = tree.keysInOrder();

        for (int i = 1; i < inOrder.size(); i++) {
            assertTrue(inOrder.get(i - 1) < inOrder.get(i), "keysInOrder must be strictly increasing");
        }

        Set<Integer> expected = new TreeSet<>(keys);
        assertEquals(new ArrayList<>(expected), inOrder);

        for (int k : expected) {
            assertTrue(tree.contains(k));
        }
    }
}
