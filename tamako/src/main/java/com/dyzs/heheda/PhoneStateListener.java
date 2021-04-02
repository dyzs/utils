package com.dyzs.heheda;

import android.telephony.TelephonyManager;

public class PhoneStateListener extends android.telephony.PhoneStateListener {
    private static final String TAG = "PhoneStateListener";

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                if (impl != null)impl.onCallRinging(phoneNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (impl != null)impl.onCallOffHook(phoneNumber);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (impl != null)impl.onCallIdle(phoneNumber);
                break;
        }
    }

    public StateImpl impl;
    public void setStateImpl(StateImpl impl) {
        this.impl = impl;
    }

    public interface StateImpl {
        void onCallRinging(String phoneNumber);

        void onCallOffHook(String phoneNumber);

        void onCallIdle(String phoneNumber);
    }
}
