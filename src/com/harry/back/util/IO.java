package com.harry.back.util;

import java.io.*;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class IO {
    public static BufferedReader getReader(String filename){
        File inFile = new File(filename);
        BufferedReader result = null;
        try {
            result = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BufferedWriter getWriter(String filename) {
        File outFile = new File(filename);
        BufferedWriter result = null;
        try {
            result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
