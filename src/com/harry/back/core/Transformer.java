package com.harry.back.core;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Transformer {
    private Pattern pcRE = Pattern.compile("pc\\s+?:\\s+?([^,]+)");
    private Pattern valRE = Pattern.compile("val\\s+?:\\s+?(\\d+)");
    private Pattern errorRE = Pattern.compile("error");
    private Type storeType = new TypeToken<Categories>(){}.getType();
    private String logPath = "error.json";
    private String outputJsonPath = "temp.json";

    private Categories categories = new Categories();

    public void transform() {
        try {
            File jsonFile = new File(logPath);
            String buffer;
            buffer = new String(Files.readAllBytes(jsonFile.toPath()));
            String[] parts = buffer.split("\\}\\s?\\{");
//            remove the redundant '{' in the first abstract state
            parts[0] = parts[0].substring(1);
//            the last two abstract states are split by '},{'
            String[] tailParts = parts[parts.length - 1].split("\\},\\{");

            List<String> abstractStates = new ArrayList<>(Arrays.asList(parts));
            abstractStates.remove(abstractStates.size() - 1);
            Collections.addAll(abstractStates, tailParts);

            int start = 0;
            int end;
            String lastPC = "";

            for (int i = 0; i < abstractStates.size(); ++i) {
                String abstractState = abstractStates.get(i);
                if (abstractState.endsWith("tick")) {
                    System.out.println(abstractState);
//                    crawl data
                    String pc;
                    int val0, val1;

                    String nextState = abstractStates.get(i+1);
                    Matcher matcher = pcRE.matcher(abstractState);
                    if (matcher.find()) {
//                        pc = matcher.group(1);
//                        remove all blanks in pc name
                        pc = matcher.group(1).replaceAll("\\s*","");
                        lastPC = pc;
//                        System.out.println(pc);
                    }
                    else
                        throw new RuntimeException("pc    " + abstractState);

                    matcher = valRE.matcher(abstractState);
                    if (matcher.find()) {
                        val0 = Integer.parseInt(matcher.group(1));
                    }
                    else
                        throw new RuntimeException("val    " + abstractState);

                    matcher = valRE.matcher(nextState);
                    if (matcher.find()) {
//                        System.out.println("val : " + matcher.group(1));
                        val1 = Integer.parseInt(matcher.group(1));
                    }
                    else
                        throw new RuntimeException("val     " + nextState);
                    end = start + val0 - val1;
                    this.categories.addIntervals(pc, start, end);
                    start = end;
                }
                else if (abstractState.endsWith("handle-interrupt")) {
                    String nextState = abstractStates.get(i + 1);
                    Matcher matcher = errorRE.matcher(nextState);
                    if (matcher.find()) {
//                        handle error
                        this.categories.moveLastIntervalToError(lastPC);
                        break;
                    }
                }
            }

            dump();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dump() {
        try {
            FileWriter fileWriter = new FileWriter(outputJsonPath);
//            add assignment so that js can import json-style file directly
            fileWriter.write("data = ");
            JsonWriter writer = new JsonWriter(fileWriter);
            Gson gson = new GsonBuilder().create();
            gson.toJson(categories, storeType, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
//        String jsonFilePath = "error.json";
        new Transformer().transform();
    }
}
