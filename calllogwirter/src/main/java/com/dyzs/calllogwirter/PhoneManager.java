package com.dyzs.calllogwirter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhoneManager {

    public static String getSysColumns(Context context) {
        Cursor temp = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        String[] names = temp.getColumnNames();
        List<String> listNames = Arrays.asList(names);
        Log.i("PhoneManager", listNames.toString());
        String ringTimes = "ring_times";
        for (int i = 0; i < listNames.size(); i++) {
            String s = listNames.get(i);
            if (s.contains("ring")) {
                ringTimes = s;
                break;
            }
        }
        Log.i("PhoneManager", ringTimes);
        while (temp.moveToNext()) {
            long id = 0;
        }
        temp.close();
        return listNames.toString() + "====" + ringTimes;
    }

    public static List<CallRecord> getCallRecords(Context context) throws Exception {
        Cursor temp = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        String[] names = temp.getColumnNames();
        List<String> listNames = Arrays.asList(names);
        Log.i("PhoneManager", listNames.toString());
        String ringTimes = "ring_times";
        for (int i = 0; i < listNames.size(); i++) {
            String s = listNames.get(i);
            if (s.contains("ring")) {
                ringTimes = s;
                break;
            }
        }
        Log.i("PhoneManager", ringTimes);
        temp.close();

        String[] columns;
        if (Build.VERSION.SDK_INT > 23) {
            columns = new String[]{
                    CallLog.Calls._ID
                    , CallLog.Calls.CACHED_NAME// 通话记录的联系人
                    , CallLog.Calls.NUMBER// 通话记录的电话号码
                    , CallLog.Calls.DATE// 通话记录的日期
                    , CallLog.Calls.DURATION// 通话时长
                    , CallLog.Calls.TYPE//类型
                    , CallLog.Calls.GEOCODED_LOCATION// 归属地}
                    , CallLog.Calls.LAST_MODIFIED
                    , CallLog.Calls.PHONE_ACCOUNT_ID
                    , ringTimes};
        } else {
            columns = new String[]{
                    CallLog.Calls._ID
                    , CallLog.Calls.CACHED_NAME// 通话记录的联系人
                    , CallLog.Calls.NUMBER// 通话记录的电话号码
                    , CallLog.Calls.DATE// 通话记录的日期
                    , CallLog.Calls.DURATION// 通话时长
                    , CallLog.Calls.TYPE//类型
                    , CallLog.Calls.GEOCODED_LOCATION// 归属地}
                    , CallLog.Calls.PHONE_ACCOUNT_ID
                    , ringTimes};
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            List<CallRecord> tempList = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, columns, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            if (cursor == null) return null;
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String geo = cursor.getString(cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION));
                String location = "";
                long lastModify = Build.VERSION.SDK_INT > 23 ? cursor.getLong(cursor.getColumnIndex(CallLog.Calls.LAST_MODIFIED)) : 0;
                String subId = cursor.getString(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));
                String rt = cursor.getString(cursor.getColumnIndex(ringTimes));
                CallRecord record = new CallRecord(id, name, number, date, duration, location, type, lastModify, subId);
                record.ring_times = rt;
                tempList.add(record);
            }
            return tempList;
        }

        return null;
    }


}
