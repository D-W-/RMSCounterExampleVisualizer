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
    private SimpleStringProperty taskID;
    private int taskNumber;

    public TaskTableData(Task task) {
        this.cycleTime = new SimpleStringProperty(task.getCyclyeTime());
        this.runningTime = new SimpleStringProperty(task.getRunningTime());
        this.taskID = new SimpleStringProperty(task.getTaskID());
    }

    public TaskTableData(final String cycleTime, final String runningTime, int taskNumber) {
        this.cycleTime = new SimpleStringProperty(cycleTime);
        this.runningTime = new SimpleStringProperty(runningTime);
        this.taskID = new SimpleStringProperty("t" + taskNumber);
        this.taskNumber = taskNumber;
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

    public String getTaskID() {
        return taskID.get();
    }

    public SimpleStringProperty taskIDProperty() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID.set(taskID);
    }

    public int getTaskNumber() {
        return taskNumber;
    }

}
