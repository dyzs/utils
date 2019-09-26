package com.example.hardcodefinder;

import java.io.File;

public class FileUtils {
    public static void createFileDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            file.mkdirs();
        }
    }
}
