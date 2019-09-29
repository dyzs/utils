package com.example.hardcodefinder;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.hardcodefinder.RegexUtils.REGEX_SET_CONTENT;
import static com.example.hardcodefinder.RegexUtils.XML_ANDROID_TEXT;


public class HardcodeFinder {

    // public static final String DEF_PATH = "D:\\knock_workspace\\knock-app-android2\\app\\src\\main\\java\\com\\knocknock\\android\\mvp";
    // public static final String DEF_PATH = "D:\\knock_workspace\\knock-app-android2\\app\\src\\main\\res\\layout";
    //public static final String DEF_PATH = "D:\\Android\\Projects\\knock-app-android2\\app\\src\\main\\java\\com\\knocknock\\android\\mvp";
    //public static final String DEF_PATH = "D:\\Android\\Projects\\knock-app-android2\\app\\src\\main\\res\\layout";
    //public static final String DEF_PATH = "D:\\dyzs_test_all_files";
    public static final String DEF_PATH = "D:\\dyzs\\utils\\hardcodefinder\\src\\main\\res\\outputStrings.xml";
    public static final String OUTPUT_PATH = "D:\\dyzs_output\\output.txt";
    public static final String OUTPUT_FILENAME = "output.txt";

    private String mResourcePath;
    private static ArrayList<String> mFilePathCache = new ArrayList<>();
    private ArrayList<String> mCacheRegexText = new ArrayList<>();
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
            mFilePathCache.clear();
            mCacheRegexText.clear();
            readFile(mResourcePath);
            parseFileListAndFind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    /**
     * 读取.java文件或者xml文件中的数据，并导出
     * @throws Exception
     */
    private void parseFileListAndFind() throws Exception {
        FileOutputStream fileOutputStream;
        File outputFile = new File(OUTPUT_PATH);
        outputFile.createNewFile();
        fileOutputStream = new FileOutputStream(outputFile);
        FileInputStream fileInputStream;
        System.out.println("-------------file read line------------mission start");
        File file;
        for (String fileAbsPath : mFilePathCache) {
            file = new File(fileAbsPath);
            if (file.isFile() && file.exists()) {
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                String line = null;
                while((line = reader.readLine()) != null) {
                    regexAndGetString(line);
                }
            }
        }
        System.out.println("-------------file read line//////////mission end + count of lines:" + mCacheRegexText.size());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        for (String s : mCacheRegexText) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
        fileOutputStream.close();
    }

    // 包含有一个中文字符，则取出当前字符串

    public static String REGEX_SET_CONTENT_STR = ".setContent";
    public static String SET_CONTENT_TEXT_STR = ".setContent";
    public static String SET_TITLE_STR = ".setTitle";
    public static String XML_ANDROID_TEXT_STR = "android:text=";
    public static String SET_CONTENT_TITLE_STR = ".setContentTitle";
    public static String ON_NEXT_STR = ".onNext";
    public static String ON_ERROR_STR = ".onError";
    public static String THROWABLE_STR = "Throwable";
    private void regexAndGetString(String line) {
        if (line == null || "".equals(line)) {
            return;
        }
        if (line.contains(REGEX_SET_CONTENT_STR) || line.contains(SET_TITLE_STR) ||
                line.contains(XML_ANDROID_TEXT_STR) || line.contains(SET_CONTENT_TITLE_STR) ||
                line.contains(ON_NEXT_STR) || line.contains(ON_ERROR_STR) || line.contains(THROWABLE_STR)) {

            String str = RegexUtils.stringFilter(REGEX_SET_CONTENT, line);
            boolean match = RegexUtils.stringFilterMatch(REGEX_SET_CONTENT, line);
            if (!"".equals(str) && match) {
                String chs = RegexUtils.gotTheChineseString(str);
                boolean b = RegexUtils.gotTheChinese(str);
                if (chs != null && !"".equals(chs)) {
                    str = str.replaceAll("\\.(setContent|setContentTitle|setTitle|setContentText|onNext|onError)\\(\"", "");
                    str = str.replaceAll("\"\\)", "");
                    System.out.println("REGEX_SET_CONTENT_STR   =======" + str);
                    mCacheRegexText.add(str);
                }
            }

            str = RegexUtils.stringFilter(XML_ANDROID_TEXT, line);
            match = RegexUtils.stringFilterMatch(XML_ANDROID_TEXT, line);
            if (!"".equals(str) && match) {
                String chs = RegexUtils.gotTheChineseString(str);
                boolean b = RegexUtils.gotTheChinese(str);
                if (chs != null && !"".equals(chs)) {
                    str = str.replaceAll("android:text=\"", "").trim();
                    str = str.replaceAll("\"", "").trim();
                    System.out.println("XML_ANDROID_TEXT_STR   =======" + str);
                    mCacheRegexText.add(str);
                }
            }
        }

        // TODO: 2019/9/27 strings xml 提取中文字符
        boolean b = RegexUtils.gotTheChinese(line);
        if (b) {
            line = line.replaceAll("</string>", "");
            line = line.replaceAll("<string name=\"", "");
            line = line.replaceAll("%s</xliff:g>", "")
                    .replaceAll("%d</xliff:g>", "")
                    .replaceAll("</item>", "");
            line = line
                    .replaceAll("&#046;","")
                    .replaceAll("&#8230;", "")
                    .replaceAll("%1\\$s", "")
                    .replaceAll("”", "").replaceAll("&gt;", "").replaceAll("</xliff:g>", "");
            int lastIndexOf = line.lastIndexOf("\">");
            if (lastIndexOf != -1) {
                line = line.substring(lastIndexOf);
                line = line.replaceAll("\">","");
                if (!"".equals(line)) {
                    System.out.println("lines===" + line + "///" + lastIndexOf);
                    mCacheRegexText.add(line.trim());
                }
            }
        }
    }


    public static void twoFilesCompareTheSameText(String file1, String file2) throws Exception {
        ArrayList<String> combineLines = new ArrayList<>();
        FileOutputStream fileOutputStream;
        File outputFile = new File(OUTPUT_PATH);
        outputFile.createNewFile();
        fileOutputStream = new FileOutputStream(outputFile);
        FileInputStream fileInputStream;
        File file = new File(file1);
        if (file.isFile() && file.exists()) {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = null;
            while((line = reader.readLine()) != null) {
                combineLines.add(line);
            }
        }

        file = new File(file2);
        if (file.isFile() && file.exists()) {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = null;
            while((line = reader.readLine()) != null) {
                combineLines.add(line);
            }
        }
        // list 去重
        System.out.println("-----------before---------mission start" + combineLines.size());
        Set<String> middleHashSet = new HashSet<>(combineLines);
        List<String> afterHashSetList = new ArrayList<>(middleHashSet);
        System.out.println("-----------after---------mission end" + afterHashSetList.size());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        for (String s : afterHashSetList) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
        fileOutputStream.close();
    }

    /**
     * 读取已翻译的字符串文件并替换输出
     *
     * @param originalTextFile
     * @param afterTranslationFile
     * @param xmlWaitToReplaceFile
     * @param outputFileAbs
     * @throws Exception
     */
    public static void readStringAndOutput(String originalTextFile, String afterTranslationFile, String xmlWaitToReplaceFile, String outputFileAbs) throws Exception {
        HashMap<String, String> originalAndTranslationHashMap = new HashMap<>();
        ArrayList<String> originalList = new ArrayList<>();
        ArrayList<String> afterTranslationList = new ArrayList<>();
        ArrayList<String> xmlStringList = new ArrayList<>();
        FileInputStream fileInputStream;
        File file = new File(originalTextFile);
        if (file.isFile() && file.exists()) {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = null;
            while((line = reader.readLine()) != null) {
                originalList.add(line.trim());
            }
        }

        file = new File(afterTranslationFile);
        if (file.isFile() && file.exists()) {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = null;
            while((line = reader.readLine()) != null) {
                afterTranslationList.add(line.trim());
            }
        }

        for (int i = 0; i < originalList.size(); i++) {
            String hashKey = originalList.get(i);
            String hashValue = afterTranslationList.get(i);
            originalAndTranslationHashMap.put(hashKey, hashValue);
        }

        // 读取string xml
        file = new File(xmlWaitToReplaceFile);
        if (file.isFile() && file.exists()) {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String line = null;
            while((line = reader.readLine()) != null) {
                xmlStringList.add(line);
            }
        }

        HashMap<String, String> replaceHash = new HashMap<>();
        for (int i = 0; i < xmlStringList.size(); i++) {
            String key = xmlStringList.get(i);
            String line = xmlStringList.get(i);
            boolean b = RegexUtils.gotTheChinese(line);
            if (b) {
                line = line.replaceAll("</string>", "");
                line = line.replaceAll("<string name=\"", "");
                line = line.replaceAll("<u>", "").replaceAll("</u>", "");
                int lastIndexOf = line.lastIndexOf("\">");
                if (lastIndexOf != -1) {
                    line = line.substring(lastIndexOf);
                    line = line.replaceAll("\">","");
                    if (!"".equals(line)) {
                        line.trim();
                    }
                }
                replaceHash.put(key, line);
            }
        }

        ArrayList<String> afterReplaceList = new ArrayList<>();
        // 遍历待替换文件
        Set<Map.Entry<String, String>> entrySet = originalAndTranslationHashMap.entrySet();
        String hashKey, hashValue, xmlStringLine;
        for (int i = 0; i < xmlStringList.size(); i++) {
            xmlStringLine = xmlStringList.get(i);

            if (replaceHash.containsKey(xmlStringLine)) {// 判断当前是否存在带替换文本
                String valueWaitToReplace = replaceHash.get(xmlStringLine);
                for (Map.Entry<String, String> entry : entrySet) {
                    hashKey = entry.getKey();
                    hashValue = entry.getValue();
                    if (valueWaitToReplace.equals(hashKey)) {// 判断带替换文本与翻译原文是否相同
                        xmlStringLine = xmlStringLine.replaceAll(hashKey, hashValue);
                    }
                }
            }
            afterReplaceList.add(xmlStringLine);
        }

        // 写出文件
        FileOutputStream fileOutputStream;
        File outputFile = new File(outputFileAbs);
        outputFile.createNewFile();
        fileOutputStream = new FileOutputStream(outputFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        for (String s : afterReplaceList) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
        fileOutputStream.close();
    }



    
}
