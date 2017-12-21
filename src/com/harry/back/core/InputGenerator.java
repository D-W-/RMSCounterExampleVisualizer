package com.harry.back.core;

import com.google.common.collect.Lists;
import com.harry.back.util.IO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class InputGenerator {
    private enum TimeUnit {
        ms,
        us
    }
    private TimeUnit timeUnit = TimeUnit.ms;
    private int interruptCycle = 1;
    private int scheduling = 1;
    private int switching = 1;
    private long macro = 1;
    private ArrayList<Task> tasks = Lists.newArrayList();


    private void setValues() {
        timeUnit = TimeUnit.us;
        interruptCycle = 5000;
        scheduling = 38;
        switching = 20;
        tasks.add(new Task(5000, 2500));
        tasks.add(new Task(10000, 1500));
        tasks.add(new Task(15000, 4500));
    }

    private void sortTasks() {
        Collections.sort(tasks);
    }

    private void checkProperty() {
        for (Task task : tasks) {
            if ( (task.runningCycle % interruptCycle) != 0) {
                throw new RuntimeException("Task running time " + task.runningCycle + " should be integer times of interrupt time");
            } else {
                task.runningCycle = task.runningCycle/interruptCycle;
            }
        }
    }

    private void calculateMacro() {
        macro = 1;
        for (Task task : tasks) {
            macro = lcm(macro, task.runningCycle);
        }
    }

    private void generateInputFile() {
        BufferedWriter bufferedWriter = IO.getWriter("test-case.maude");
        try {
            bufferedWriter.write("set trace off ."); bufferedWriter.newLine();
            bufferedWriter.write("set break off ."); bufferedWriter.newLine();
            bufferedWriter.write("set profile off ."); bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("(tomod INSTANTIATION is "); bufferedWriter.newLine();
            bufferedWriter.write("  protecting POSRAT-TIME-DOMAIN-WITH-INF ."); bufferedWriter.newLine();
            bufferedWriter.write("  including INTERRUPT-SOURCE ."); bufferedWriter.newLine();
            bufferedWriter.write("  including PERIODIC-TASK ."); bufferedWriter.newLine();
            bufferedWriter.write("  including SYSTEM ."); bufferedWriter.newLine();
            bufferedWriter.write("  including RMS-VERIFICATION ."); bufferedWriter.newLine();
            bufferedWriter.write("  including RMS-MODEL-CHECK ."); bufferedWriter.newLine();
            bufferedWriter.write("  protecting QID ."); bufferedWriter.newLine();
            bufferedWriter.write("  subsort Qid < Oid ."); bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("  op isrc : -> Object ."); bufferedWriter.newLine();
            bufferedWriter.write("  eq isrc = < 'isrc : IntSrc | val : 0 , cycle : " + Integer.toString(interruptCycle) + " > .");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("  op sts : -> SysTasks ."); bufferedWriter.newLine();
            bufferedWriter.write(
                    String.format("  eq sts = [ < scheduling : Task | cnt : [ 0 / [ %d , %d ] ] > < switching : Task | cnt : [ 0 / [ %d , %d ] ] > ] .",
                            scheduling, scheduling, switching, switching));
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("  op hw : -> Hardware ."); bufferedWriter.newLine();
            bufferedWriter.write("  eq hw = [ < 'regs : Regs | pc : none , mask : false , ir : false , temp : none > ; bottom ] ."); bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("  op tasklist : -> TaskList ."); bufferedWriter.newLine();
            int N = tasks.size();
            String[] strTasks = new String[N];
            for (int i = 0; i < N; ++i) {
                strTasks[i] = String.format("< 't%d : PTask | priority : %d , period : %d , status : DORMANT , cnt : [ 0 / [ %d , %d ] ] >",
                        i + 1, N - i, tasks.get(i).runningCycle, tasks.get(i).runningTime, tasks.get(i).runningTime);
            }
            bufferedWriter.write("  eq tasklist = ");
            for (int i = 0; i < N; i++) {
                bufferedWriter.write(strTasks[i]); bufferedWriter.newLine();
                bufferedWriter.write("          :: ");
            }
            bufferedWriter.write("null ."); bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("  op init : -> GlobalSystem ."); bufferedWriter.newLine();
            bufferedWriter.write("  eq init = { tasklist [ 0 / " + Long.toString(macro) + " ] sts hw isrc } .");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("endtom)"); bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("(set tick max .)"); bufferedWriter.newLine();
            bufferedWriter.write("(mc init |=u []schedulable .)"); bufferedWriter.newLine();
            bufferedWriter.write("quit"); bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                bufferedWriter.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    public void generate() {
        setValues();
        sortTasks();
        checkProperty();
        calculateMacro();
        generateInputFile();
    }

    public static void main(String[] args) {
//        ArrayList<Task> tasks = Lists.newArrayList();
//        tasks.add(new Task(10, 0));
//        tasks.add(new Task(8, 0));
//        tasks.add(new Task(9, 0));
//        tasks.add(new Task(7, 0));
//        Collections.sort(tasks);
//        System.out.println(tasks);
        new InputGenerator().generate();
    }


    private long lcm(long a, long b) {
        long m = a, n = b;
    /* a, b不相等，大数减小数，直到相等为止。*/
        while (a != b) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
        return (m * n / a);
    }

}
