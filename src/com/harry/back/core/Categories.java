package com.harry.back.core;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Map;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Categories {

    private Map<String, JsonArray> intervalsMap = Maps.newHashMap();
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
}
