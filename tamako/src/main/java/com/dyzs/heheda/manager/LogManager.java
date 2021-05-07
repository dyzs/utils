package com.dyzs.heheda.manager;


import com.dyzs.heheda.CommonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 拨号时间:date
 * token 开始时间:date
 * 挂断时间:date
 * token 结束时间与结果:[code]+[date]
 */
public class LogManager {
    private static LogManager INSTANCE;
    private final static Object sLock = new Object();
    public static LogManager getInstance() {
        if (INSTANCE == null) {
            synchronized (sLock) {
                INSTANCE = new LogManager();
            }
        }
        return INSTANCE;
    }

    private List<MyLog> cacheLogList;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    private String logName;
    private LogManager() {
        cacheLogList = new ArrayList<>();
    }

    public void addLog(MyLog log) {
        cacheLogList.add(log);
    }

    public void preparedForStart(String logName) {
        setLogName(logName);
        cacheLogList.clear();
    }


    public synchronized List<String> getWriteListAndClearCache() {
        List<String> list = new ArrayList<>();
        for (MyLog log : cacheLogList) {
            list.add(log.logMsg);
        }
        cacheLogList.clear();
        return list;
    }

    public synchronized List<String> getWriteListAndClearCacheForCallIn() {
        List<String> list = new ArrayList<>();
        for (MyLog log : cacheLogList) {
            list.add(log.logMsg);
        }
        cacheLogList.clear();
        return list;
    }

    public void writeLog(List<String> list) {
        CommonUtils.writeLog(list, getLogName());
    }

    /**
     * 已过时
     */
    @Deprecated
    public synchronized void writeLog() {
        List<String> list = new ArrayList<>();
        for (MyLog log : cacheLogList) {
            list.add(log.logMsg);
        }
        CommonUtils.writeLog(list, getLogName());
    }

    public String getNetStringLog() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MyLog log : cacheLogList) {
            stringBuilder.append(log.logMsg).append("</br>");
        }
        return stringBuilder.toString();
    }

    public static class MyLog {
        private String logCode;
        private String logMsg = "";
        private String id;
        public MyLog() {}

        public MyLog(String logCode, String logMsg) {
            this.id = String.valueOf(System.currentTimeMillis());
            this.logCode = logCode == null ? "" : logCode;
            this.logMsg = logMsg;
        }
    }
}
