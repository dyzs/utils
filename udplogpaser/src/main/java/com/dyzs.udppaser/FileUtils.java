package com.dyzs.udppaser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.dyzs.udppaser.DateUtils.getTimeForFileName;

public class FileUtils {
    private static final String PATH = "D:/tokenLog/";
    private static final String NAME = "TokenLog";
    private static final String LOG_SUFFIX = ".log";
    public static void writeLog(List<String> infos) {
        writeLog(infos, getTimeForFileName());
    }
    public static void writeLog(List<String> infos, String logName) {
        try {
            FileOutputStream fileOutputStream;
            File file = new File(PATH, NAME + "_" + logName + LOG_SUFFIX);
            if (file.isFile() && file.exists()) {
                fileOutputStream = new FileOutputStream(file, true);
            } else {
                createFile(file);
                fileOutputStream = new FileOutputStream(file);
            }
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            for (String info : infos) {
                writer.write(info);
                writer.write("\n");
                writer.flush();
            }
            writer.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFile(File file) throws Exception {
        if (file.getParentFile().exists()) {
            file.createNewFile();
        } else {
            file.getParentFile().mkdirs();
            createFile(file);
        }
    }

}
