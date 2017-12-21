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



    public void crawl(String filename) {
        String command = "./res/maude.linux64 res/real-time-maude.maude res/RMS.maude " + filename + " > temp";
        Execute.executeCommand(command);

        BufferedReader bufferedReader = IO.getReader("temp");
        BufferedWriter bufferedWriter = IO.getWriter("error.json");
        boolean writeLine = false;
        String line = null;
        try {
            while((line = bufferedReader.readLine()) != null){
                if (line.startsWith("Bye"))
                    writeLine = false;
                if (writeLine)
                    bufferedWriter.write(line);
                if (line.startsWith("Result ModelCheckResult"))
                    writeLine = true;
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

    }

    public static void main(String[] args) {
        new Crawler().crawl("example-false.maude");
        new Transformer().transform();
    }
}
