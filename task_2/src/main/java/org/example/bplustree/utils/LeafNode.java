package org.example.bplustree.utils;

import org.example.bplustree.BPlusTree;

public class LeafNode extends Node {
    public LeafNode next;

    @Override
    public boolean isLeaf() {
        return true;
    }
}
