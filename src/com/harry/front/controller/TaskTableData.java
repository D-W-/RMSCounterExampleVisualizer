package com.harry.front.controller;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */

import javafx.beans.property.SimpleStringProperty;
import com.harry.front.data.Task;


public class TaskTableData {

    private SimpleStringProperty cycleTime;
    private SimpleStringProperty runningTime;

    public TaskTableData(Task task) {
        this.cycleTime = new SimpleStringProperty(task.getCyclyeTime());
        this.runningTime = new SimpleStringProperty(task.getRunningTime());
    }

    public TaskTableData(final String cycleTime, final String runningTime) {
        this.cycleTime = new SimpleStringProperty(cycleTime);
        this.runningTime = new SimpleStringProperty(runningTime);
    }


    public String getCycleTime() {
        return cycleTime.get();
    }

    public SimpleStringProperty cycleTimeProperty() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime.set(cycleTime);
    }

    public String getRunningTime() {
        return runningTime.get();
    }

    public SimpleStringProperty runningTimeProperty() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime.set(runningTime);
    }
}
