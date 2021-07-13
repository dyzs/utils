package com.dyzs.calllogwirter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
    private static ExecutorService mExecutor;
    public static void enqueue(Runnable runnable){
        if(mExecutor==null) mExecutor= Executors.newScheduledThreadPool(4);
        mExecutor.execute(runnable);
    }

    private static int cpuNumber = 6;
    private static ThreadPoolExecutor writeLogExecutor;
    public static ThreadPoolExecutor getThreadPoolExecutorForWriteLog(int queueCapacity) {
        if (writeLogExecutor == null) writeLogExecutor = new ThreadPoolExecutor(
                cpuNumber,
                cpuNumber * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueCapacity));
        return writeLogExecutor;
    }
}
