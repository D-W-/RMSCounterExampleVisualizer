package com.harry.back.core;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Task implements Comparable<Task> {

    public Integer runningCycle;
    public Integer runningTime;


    public Task(int runningCycle, int runningTime) {
        this.runningCycle = runningCycle;
        this.runningTime = runningTime;
    }

    @Override
    public int compareTo(Task task) {
        return runningCycle.compareTo(task.runningCycle);
    }

    @Override
    public String toString() {
        return Integer.toString(runningCycle);
    }
}
