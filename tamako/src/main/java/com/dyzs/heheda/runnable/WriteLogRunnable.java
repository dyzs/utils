package com.dyzs.heheda.runnable;

import com.dyzs.heheda.TamakoUtils;

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
        TamakoUtils.writeLog(list, logFileName);
    }
}
