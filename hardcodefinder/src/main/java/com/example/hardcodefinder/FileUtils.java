package com.example.hardcodefinder;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static void createFileDir(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
            } else {
                file.getParentFile().mkdirs();
                createFileDir(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
