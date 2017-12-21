package com.harry.back.cmd;

import com.harry.core.Transformer;
import com.harry.util.Execute;
import com.harry.util.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import static com.harry.util.IO.getWriter;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Crawler {



    public void crawl(String filename) {
        String command = "./maude.linux64 real-time-maude.maude RMS.maude " + filename + " > temp";
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
