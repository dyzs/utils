package com.dyzs.aidl.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.dyzs.aidl.IMyAidlInterface;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    static class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getEncryptPwd() throws RemoteException {
            return "this is a value of encrypt";
        }

        @Override
        public void testSetDefaultDialer() throws RemoteException {

        }
    }
}
