package com.harry.back.cmd;

import com.google.common.collect.Lists;
import com.harry.back.core.Transformer;
import com.harry.back.util.Execute;
import com.harry.back.util.IO;

import com.harry.back.core.Transformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Crawler {

    public boolean crawl(String filename) {

        String osName = System.getProperty("os.name").toLowerCase();
//        String command = "";
      String platform = "";
        List<String> commands = Lists.newArrayList();
        if (osName.indexOf("win") != -1) {
            BufferedReader br = IO.getReader("RMSLOCATION");
            try {
                String loc = br.readLine();
                platform = "\"" + loc + "\"";
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
              try {
                br.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        }
        else {
            if (osName.indexOf("linux") != -1) {
                if (System.getProperty("os.arch").indexOf("amd64") != -1)
                    platform = "maude.linux64";
                else
                    platform = "maude.linux";
            }
            else if (osName.indexOf("mac") != -1) {
                platform = "maude.intelDarwin";
            }
            platform = "./res/" + platform;
        }
        commands.add(platform);
        commands.addAll(Arrays.asList("res/real-time-maude.maude", "res/RMS.maude", filename));
        Execute.executeCommand(new LinkedList<String>(Arrays.asList("rm", "temp")));
        Execute.executeCommand(commands, "temp");

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
        new Crawler().crawl("res/test-case.maude");
        new Transformer().transform();
        String osName = System.getProperty("os.name").toLowerCase();
        String temp = "Mac OS X".toLowerCase();
        System.out.println(temp.indexOf("linux"));
        System.out.println(osName);
    }
}
