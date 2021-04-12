// IMyAidlInterface.aidl
package com.dyzs.heheda;

// Declare any non-default types here with import statements

interface ICallService {
    /**
     * 重置参数
     */
     void resetParam(int callTimes, String callPhone);

    /**
     * 开启任务
     */
     void startCallTask();

    /**
     * 结束任务
     */
     void endCallTask();

    /**
     * 挂断
     */
     void hangUp();

     /**
      * 发送ck token
      */
      void sendCKToken();


}
