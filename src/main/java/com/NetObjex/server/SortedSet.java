package com.NetObjex.server;

import java.util.List;

public interface SortedSet {
    void put(int setId, int key, int score);
    void remove(int setId, int key);
    int size(int setId);
    int getKeyValue(int setId, int key);
    List<Integer> getRange(int lower, int upper, int... setIds);
}
