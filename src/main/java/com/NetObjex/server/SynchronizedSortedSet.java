package com.NetObjex.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class SynchronizedSortedSet implements SortedSet {

    private final Map<Integer, Integer> setIdToScoreMap;
    private final Map<Integer, Set<Integer>> setIdToSetMap;

    SynchronizedSortedSet() {
        setIdToSetMap = Collections.synchronizedMap(new TreeMap<>());
        setIdToScoreMap = Collections.synchronizedMap(new TreeMap<>());
    }

    @Override
    public void put(int setId, int key, int score) {
            boolean setExists = isExistingSet(setId);
            if (setExists) {
                addKey(setId, key, score);
            } else {
                setIdToScoreMap.put(setId, score);
                Set<Integer> set = new TreeSet<>(Collections.singletonList(key));
                setIdToSetMap.put(key, set);
            }
    }

    private void addKey(int setId, int key, int score) {
        Set<Integer> set = setIdToSetMap.get(setId);
        if (set.contains(key)) {
            setIdToScoreMap.put(setId, score);
        } else {
            set.add(key);
        }
    }

    @Override
    public void remove(int setId, int key) {
        synchronized (setIdToScoreMap) {
            boolean setExists = isExistingSet(setId);
            if (setExists) {
                Set<Integer> set = setIdToSetMap.get(setId);
                set.remove(key);
            }
        }
    }

    @Override
    public int size(int setId) {
        synchronized (setIdToScoreMap) {
            boolean setExists = isExistingSet(setId);
            if (setExists) {
                return setIdToSetMap.get(setId).size();
            }
            return 0;
        }
    }

    @Override
    public int getKeyValue(int setId, int key) {
        synchronized (setIdToScoreMap) {
            if (!isExistingSet(setId)) {
                return 0;
            }
            if (setIdToSetMap.get(setId).contains(key)) {
                return setIdToScoreMap.get(key);
            }
            return 0;
        }
    }

    @Override
    public List<Integer> getRange(int lower, int upper, int... setIds) {
        synchronized (setIdToScoreMap) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int setId : setIds) {
                if (setIdToScoreMap.containsKey(setId))
                    map.put(setId, setIdToScoreMap.get(setId));
            }
            map = sortByKey(map);
            return getMapToList(map);
        }
    }

    private List<Integer> getMapToList(Map<Integer, Integer> map) {
        List<Integer> list = new ArrayList<>(map.size() * 2);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            list.add(entry.getKey());
            list.add(entry.getValue());
        }
        return list;
    }

    private Map<Integer, Integer> sortByKey(Map<Integer, Integer> map) {
        return map.entrySet().stream().
                sorted(comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private boolean isExistingSet(int setId) {
        return setIdToScoreMap.containsKey(setId);
    }

}
