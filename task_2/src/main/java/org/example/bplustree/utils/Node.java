package org.example.bplustree.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Node {
    public final List<Integer> keys = new ArrayList<>();

    public abstract boolean isLeaf();

    List<Integer> viewKeys() {
        return Collections.unmodifiableList(keys);
    }
}
