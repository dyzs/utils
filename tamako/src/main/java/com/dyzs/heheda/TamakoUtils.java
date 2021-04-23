package com.dyzs.heheda;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TamakoUtils {
    public static String getTimeForFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA);
        return format.format(new Date(time));
    }

    public static boolean checkLacks(Context context) {
        return lacksPermissions(context, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE);
    }

    public static boolean lacksPermissions(Context mContexts, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/tokenLog/";
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
