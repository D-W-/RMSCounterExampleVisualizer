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

