package org.example.bplustree.utils;

import java.util.ArrayList;
import java.util.List;

public class InternalNode extends Node {
    public final List<Node> children = new ArrayList<>();

    @Override
    public boolean isLeaf() {
        return false;
    }
}
