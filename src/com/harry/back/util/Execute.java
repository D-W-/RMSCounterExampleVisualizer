package com.harry.back.util;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */

import java.io.*;
import java.util.List;

public class Execute {
    /*
     * 这个类是专门用来执行某条指令的
     */
    public static String[] paths = null;

    public static void executeCommand(List<String> commands){
        try {
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void executeCommand(List<String> commands, String loc) {
        try {
            ProcessBuilder builder = new ProcessBuilder(commands);
//            File file = new File(loc);
//            file.delete();
            builder.redirectOutput(new File(loc));
            Process process = builder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String executeCommandandGetoutput(String command){
//		执行一条linux命令 如果命令执行不成功 就直接产生一个异常
        Runtime runtime = Runtime.getRuntime();
        String buffer = "";
        try {
            Process process = runtime.exec(new String[] {"/bin/sh","-c",command});
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine()) != null){
                buffer  = buffer + line + System.getProperty("line.separator");
            }
            br.close();
            isr.close();
            int waitID = process.waitFor();
            if(waitID != 0){
                isr = new InputStreamReader(process.getErrorStream());
                br = new BufferedReader(isr);
                line = null;
                while((line = br.readLine()) != null){
                    buffer  = buffer + line + System.getProperty("line.separator");
                }
                br.close();
                isr.close();
                throw new InterruptedException("shell failed!\n" + buffer);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static boolean executeCommands(String[] commands) {
//		创建一个临时的 bash 文件来执行,可以执行 extglob 的语句 也可以执行多条语句
        boolean result = true;
        File tempScript = null;
        try {
            tempScript = createTempScript(commands);
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            if(paths != null){
//	        	加入 PATH 环境变量
                String envPath = pb.environment().get("PATH");
                for(String path : paths){
                    envPath += (":" + path);
                }
                pb.environment().put("PATH", envPath);
            }
            pb.inheritIO();
            Process process = pb.start();
            int waitID = process.waitFor();
            if(waitID != 0){
                InputStreamReader isr = new InputStreamReader(process.getErrorStream());
                BufferedReader br = new BufferedReader(isr);
                String buffer = "";
                String line = null;
                while((line = br.readLine()) != null){
                    buffer  = buffer + line + System.getProperty("line.separator");
                }
                br.close();
                isr.close();
//				throw new RuntimeException("shell failed!\n" + buffer);
                System.err.println("shell failed!\n" + buffer);
                result = false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result = false;
        } finally {
            tempScript.delete();
        }
        return result;
    }

    public static File createTempScript(String[] commands) throws IOException {
//		创建文件
        File tempScript = File.createTempFile("processMakefileScript", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        for(int i = 0;i < commands.length;++i){
            printWriter.println(commands[i]);
        }
        printWriter.flush();
        printWriter.close();

        return tempScript;
    }
}
