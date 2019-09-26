package com.example.hardcodefinder;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class HardcodeFinder {

    // public static final String DEF_PATH = "D:\\knock_workspace\\knock-app-android2\\app\\src\\main\\java\\com\\knocknock\\android\\mvp";
    public static final String DEF_PATH = "D:\\knock_workspace\\knock-app-android2\\app\\src\\main\\res\\layout";
    public static final String OUTPUT_PATH = "D:\\dyzs_output\\output.txt";
    public static final String OUTPUT_FILENAME = "output.txt";

    private String mResourcePath;
    private static ArrayList<String> mFilePathCache = new ArrayList<>();
    public HardcodeFinder() {
        System.out.println("is ready for getting start");
        mResourcePath = DEF_PATH;
    }

    public HardcodeFinder(String resourcePath) {
        System.out.println("is ready for getting start");
        if (resourcePath == null || "".equals(resourcePath)) {
            mResourcePath = DEF_PATH;
            return;
        }
        this.mResourcePath = resourcePath;
    }

    public void operateFind() {
        System.out.println("start operate, file path:[" + mResourcePath + "]");
        try {
            countOfFile = 0;
            mFilePathCache.clear();
            readFile(mResourcePath);
            parseFileListAndFind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int countOfFile = 0;
    private void readFile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                mFilePathCache.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                String[] fileList = file.list();
                if (fileList == null)fileList = new String[]{};
                for (String s : fileList) {
                    File readFile = new File(filepath + "\\" + s);
                    if (!readFile.isDirectory()) {
                        mFilePathCache.add(readFile.getAbsolutePath());
                    } else if (readFile.isDirectory()) {
                        readFile(filepath + "\\" + s);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parseFileListAndFind() throws Exception {
        FileOutputStream fileOutputStream;
        File outputFile = new File(OUTPUT_PATH);
        if (outputFile.isFile() && outputFile.exists()) {
            fileOutputStream = new FileOutputStream(outputFile, true);
        } else {
            FileUtils.createFileDir(outputFile);
            fileOutputStream = new FileOutputStream(outputFile);
        }
        FileInputStream fileInputStream;
        System.out.println("-------------file read line------------mission start");
        for (String fileAbsPath : mFilePathCache) {
            File file = new File(fileAbsPath);
            if (file.isFile() && file.exists()) {
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                String line = null;
                while((line = reader.readLine()) != null) {
                    regexAndGetString(line);
                }
            }
        }
        System.out.println("-------------file read line//////////mission end");
    }

    // 包含有一个中文字符，则取出当前字符串
    public static String REGEX_SET_CONTENT = "\\.setContent\\(\"(.)*[\\u4e00-\\u9fa5](.)*\"\\)";
    public static String SET_TITLE = "\\.setTitle\\(\"(.)*[\\u4e00-\\u9fa5](.)*\"\\)";
    public static String XML_ANDROID_TEXT = "android:text\\=\"(.)*\"";

    /**
     * 字符串优先匹配
     */
    public static String REGEX_SET_CONTENT_STR = ".setContent";
    public static String SET_TITLE_STR = ".setTitle";
    public static String XML_ANDROID_TEXT_STR = "android:text=";
    private void regexAndGetString(String line) {
        if (line == null || "".equals(line)) {
            return;
        }
        if (line.contains(REGEX_SET_CONTENT_STR) || line.contains(SET_TITLE_STR) || line.contains(XML_ANDROID_TEXT_STR)) {
            String str = RegexUtils.stringFilter(REGEX_SET_CONTENT, line);
            // System.out.println(line);
            if (!"".equals(str)) {
                String chs = RegexUtils.gotTheChineseString(str);
            }
            str = RegexUtils.stringFilter(SET_TITLE, line);
            if (!"".equals(str)) {
                // System.out.println("REGEXXXXXXXXXXXXXXXXXXXXX:" + str);
            }
            str = RegexUtils.stringFilter(XML_ANDROID_TEXT, line);
            if (!"".equals(str)) {
                System.out.println("REGEXXXXXXXXXXXXXXXXXXXXX:" + str);
                String chs = RegexUtils.gotTheChineseString(str);
                if (!"".equals(chs)) {
                    System.out.println("chinese:" + chs);
                }
            }
        }
    }


    public static void twoFilesCompareTheSameText(String file1, String file2) {

    }

}
