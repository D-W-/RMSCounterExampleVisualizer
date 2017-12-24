package com.harry.back.core;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Categories {

    private Map<String, JsonArray> intervalsMap = Maps.newTreeMap();
    private JsonArray errorArray = new JsonArray();

    public void addIntervals(String id, int start, int end) {
        JsonArray array;
        if (intervalsMap.containsKey(id)) {
            array = intervalsMap.get(id);
        }
        else {
            array = new JsonArray();
            intervalsMap.put(id, array);
        }
        JsonArray interval = new JsonArray();
        interval.add(start);
        interval.add(end);
        array.add(interval);
    }

    public void moveLastIntervalToError(String id) {
        if (intervalsMap.containsKey(id)) {
            JsonArray array = intervalsMap.get(id);
            JsonElement interval = array.get(array.size() - 1);
            this.errorArray.add(interval);
        }
    }

    public void sortCategories() {
        SortedSet<String> keys = new TreeSet<>(intervalsMap.keySet());
        Map<String, JsonArray> temp = Maps.newTreeMap();
        temp.putAll(intervalsMap);
        intervalsMap.clear();
        for (String key : keys) {
            intervalsMap.put(key, temp.get(key));
            System.out.println(key);
        }
    }
}

