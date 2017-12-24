package com.harry.front.data;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Task implements Comparable<Task> {

    private String cyclyeTime;
    private String runningTime;

    public Integer cycle;
    public Integer running;

    public Task(String cyclyeTime, String runningTime) {
        super();
        this.cyclyeTime = cyclyeTime;
        this.runningTime = runningTime;

    }

    public Task(int cycle, int running) {
        this.cycle = cycle;
        this.running = running;
    }

    public Task transfer() {
        this.cycle = Integer.parseInt(cyclyeTime);
        this.running = Integer.parseInt(runningTime);
        return this;
    }

    public String getCyclyeTime() {
        return cyclyeTime;
    }

    public void setCyclyeTime(String cyclyeTime) {
        this.cyclyeTime = cyclyeTime;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public String toString() {
        return "cycleTime: " + cyclyeTime + ", runningTime: " + runningTime;
    }

    @Override
    public int compareTo(Task task) {
        return cycle.compareTo(task.cycle);
    }

}
