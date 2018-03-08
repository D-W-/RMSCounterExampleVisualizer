package com.harry.back.cmd;

import com.harry.back.core.Transformer;
import com.harry.back.util.Execute;
import com.harry.back.util.IO;

import com.harry.back.core.Transformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Crawler {

    public boolean crawl(String filename) {
        String platform = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("linux") != -1) {
            if (System.getProperty("os.arch").indexOf("amd64") != -1)
                platform = "maude.linux64";
            else
                platform = "maude.linux";
        }
        else if (osName.indexOf("mac") != -1) {
            platform = "maude.intelDarwin";
        }
        Execute.executeCommand("rm temp");
        String command = "./res/" + platform + " real-time-maude.maude RMS.maude " + filename + " > temp";
        Execute.executeCommand(command);

        BufferedReader bufferedReader = IO.getReader("temp");
        BufferedWriter bufferedWriter = IO.getWriter("error.json");
        boolean writeLine = false;
        boolean wrote = false;
        String line = null;
        try {
            while((line = bufferedReader.readLine()) != null){
                if (line.startsWith("Bye"))
                    writeLine = false;
                if (writeLine)
                    bufferedWriter.write(line);
                if (line.startsWith("Result ModelCheckResult")) {
                    writeLine = true;
                    wrote = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                bufferedReader.close();
                bufferedWriter.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return !wrote;
    }

    public static void main(String[] args) {
//        new Crawler().crawl("res/test-case.maude");
//        new Transformer().transform();
        String osName = System.getProperty("os.name").toLowerCase();
        String temp = "Mac OS X".toLowerCase();
        System.out.println(temp.indexOf("linux"));
        System.out.println(osName);
    }
}
