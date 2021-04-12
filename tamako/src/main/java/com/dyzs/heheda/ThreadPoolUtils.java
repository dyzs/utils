package com.dyzs.heheda;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
    private static ExecutorService mExecutor;
    public static void enqueue(Runnable runnable){
        if(mExecutor==null) mExecutor= Executors.newScheduledThreadPool(4);
        mExecutor.execute(runnable);
    }

}
