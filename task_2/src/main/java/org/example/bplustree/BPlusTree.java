package org.example.bplustree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * B+ дерево для целочисленных ключей.
 *
 * Ограничение по заданию: максимум ключей в узле = 7.
 * Реализованы операции вставки, поиска и обхода листьев.
 *
 * Для модульного тестирования предусмотрена трассировка
 * прохождения характерных точек алгоритма вставки.
 */
public final class BPlusTree {
    public static final int REQUIRED_MAX_KEYS = 7;

    private final int maxKeys;
    private Node root;

    public BPlusTree() {
        this(REQUIRED_MAX_KEYS);
    }

    public BPlusTree(int maxKeys) {
        if (maxKeys < 3) {
            throw new IllegalArgumentException("maxKeys must be >= 3");
        }
        this.maxKeys = maxKeys;
        this.root = new LeafNode();
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void insert(int key) {
        insertInternal(key, null);
    }

    public List<String> insertWithTrace(int key) {
        List<String> trace = new ArrayList<>();
        insertInternal(key, trace);
        return trace;
    }

    public List<String> insertAllWithTrace(List<Integer> keys) {
        List<String> trace = new ArrayList<>();
        for (int key : keys) {
            insertInternal(key, trace);
        }
        return trace;
    }

    public boolean contains(int key) {
        LeafNode leaf = findLeaf(key);
        int pos = findInsertPosition(leaf.keys, key);
        return pos < leaf.keys.size() && leaf.keys.get(pos) == key;
    }

    public List<Integer> keysInOrder() {
        List<Integer> result = new ArrayList<>();
        LeafNode current = leftMostLeaf();
        while (current != null) {
            result.addAll(current.keys);
            current = current.next;
        }
        return result;
    }

    private void insertInternal(int key, List<String> trace) {
        record(trace, "START_INSERT");

        Deque<InternalNode> parents = new ArrayDeque<>();
        Deque<Integer> parentChildIndexes = new ArrayDeque<>();

        Node current = root;
        if (current.isLeaf()) {
            record(trace, "ROOT_IS_LEAF");
        }

        while (!current.isLeaf()) {
            InternalNode internal = (InternalNode) current;
            int childIndex = childIndexForKey(internal.keys, key);
            record(trace, "DESCEND_INTERNAL");
            parents.push(internal);
            parentChildIndexes.push(childIndex);
            current = internal.children.get(childIndex);
        }

        LeafNode leaf = (LeafNode) current;
        record(trace, "LEAF_FOUND");

        int insertPos = findInsertPosition(leaf.keys, key);
        if (insertPos < leaf.keys.size() && leaf.keys.get(insertPos) == key) {
            record(trace, "DUPLICATE_KEY");
            record(trace, "END_INSERT");
            return;
        }

        leaf.keys.add(insertPos, key);
        record(trace, "LEAF_INSERT");

        if (leaf.keys.size() > maxKeys) {
            record(trace, "LEAF_OVERFLOW");
            splitLeafAndPropagate(leaf, parents, parentChildIndexes, trace);
        }

        record(trace, "END_INSERT");
    }

    private void splitLeafAndPropagate(
        LeafNode leaf,
        Deque<InternalNode> parents,
        Deque<Integer> parentChildIndexes,
        List<String> trace
    ) {
        int splitIndex = leaf.keys.size() / 2;

        LeafNode rightLeaf = new LeafNode();
        rightLeaf.keys.addAll(leaf.keys.subList(splitIndex, leaf.keys.size()));
        leaf.keys.subList(splitIndex, leaf.keys.size()).clear();

        rightLeaf.next = leaf.next;
        leaf.next = rightLeaf;

        record(trace, "SPLIT_LEAF");

        int separator = rightLeaf.keys.get(0);
        insertIntoParent(leaf, separator, rightLeaf, parents, parentChildIndexes, trace);
    }

    private void insertIntoParent(
        Node left,
        int separator,
        Node right,
        Deque<InternalNode> parents,
        Deque<Integer> parentChildIndexes,
        List<String> trace
    ) {
        if (parents.isEmpty()) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(separator);
            newRoot.children.add(left);
            newRoot.children.add(right);
            root = newRoot;
            record(trace, "NEW_ROOT");
            return;
        }

        InternalNode parent = parents.pop();
        int childIndex = parentChildIndexes.pop();

        record(trace, "INSERT_IN_PARENT");

        parent.keys.add(childIndex, separator);
        parent.children.add(childIndex + 1, right);

        if (parent.keys.size() > maxKeys) {
            record(trace, "INTERNAL_OVERFLOW");
            splitInternalAndPropagate(parent, parents, parentChildIndexes, trace);
        }
    }

    private void splitInternalAndPropagate(
        InternalNode node,
        Deque<InternalNode> parents,
        Deque<Integer> parentChildIndexes,
        List<String> trace
    ) {
        int middleIndex = node.keys.size() / 2;
        int separator = node.keys.get(middleIndex);

        InternalNode rightNode = new InternalNode();
        rightNode.keys.addAll(node.keys.subList(middleIndex + 1, node.keys.size()));
        rightNode.children.addAll(node.children.subList(middleIndex + 1, node.children.size()));

        node.keys.subList(middleIndex, node.keys.size()).clear();
        node.children.subList(middleIndex + 1, node.children.size()).clear();

        record(trace, "SPLIT_INTERNAL");

        insertIntoParent(node, separator, rightNode, parents, parentChildIndexes, trace);
    }

    private LeafNode findLeaf(int key) {
        Node current = root;
        while (!current.isLeaf()) {
            InternalNode internal = (InternalNode) current;
            current = internal.children.get(childIndexForKey(internal.keys, key));
        }
        return (LeafNode) current;
    }

    private LeafNode leftMostLeaf() {
        Node current = root;
        while (!current.isLeaf()) {
            current = ((InternalNode) current).children.get(0);
        }
        return (LeafNode) current;
    }

    private int childIndexForKey(List<Integer> keys, int key) {
        int index = 0;
        while (index < keys.size() && key >= keys.get(index)) {
            index++;
        }
        return index;
    }

    private int findInsertPosition(List<Integer> keys, int key) {
        int index = 0;
        while (index < keys.size() && keys.get(index) < key) {
            index++;
        }
        return index;
    }

    private void record(List<String> trace, String point) {
        if (trace != null) {
            trace.add(point);
        }
    }

    private abstract static class Node {
        final List<Integer> keys = new ArrayList<>();

        abstract boolean isLeaf();

        List<Integer> viewKeys() {
            return Collections.unmodifiableList(keys);
        }
    }

    private static final class InternalNode extends Node {
        final List<Node> children = new ArrayList<>();

        @Override
        boolean isLeaf() {
            return false;
        }
    }

    private static final class LeafNode extends Node {
        LeafNode next;

        @Override
        boolean isLeaf() {
            return true;
        }
    }
}
