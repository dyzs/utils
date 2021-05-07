package com.dyzs.heheda.runnable;

import com.dyzs.heheda.CommonUtils;

import java.util.List;

public class WriteLogRunnable implements Runnable {
    List<String> list;
    String logFileName;
    public WriteLogRunnable(List<String> list, String logFileName) {
        this.list = list;
        this.logFileName = logFileName;
    }
    @Override
    public void run() {
        CommonUtils.writeLog(list, logFileName);
    }
}
