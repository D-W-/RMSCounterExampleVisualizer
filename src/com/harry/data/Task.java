package com.harry.data;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Task {

    private String cyclyeTime;
    private String runningTime;

    public Task(String cyclyeTime, String runningTime) {
        super();
        this.cyclyeTime = cyclyeTime;
        this.runningTime = runningTime;
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
}
