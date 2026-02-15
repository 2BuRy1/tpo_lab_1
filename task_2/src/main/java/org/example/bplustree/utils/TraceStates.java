package org.example.bplustree.utils;

public enum TraceStates {
    START_INSERT,
    DESCEND_INTERNAL,
    DUPLICATE_KEY,
    LEAF_FOUND,
    LEAF_INSERT,
    LEAF_OVERFLOW,
    SPLIT_LEAF,
    INSERT_IN_PARENT,
    INTERNAL_OVERFLOW,
    SPLIT_INTERNAL,
    NEW_ROOT,
    END_INSERT,
    ROOT_IS_LEAF,
}
