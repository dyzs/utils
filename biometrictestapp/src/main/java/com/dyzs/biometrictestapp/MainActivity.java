package com.dyzs.biometrictestapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.dyzs.app.LockScreenManager;
import com.dyzs.biometric.BiometricPrompt;
import com.dyzs.biometric.auth.AuthPromptCallback;
import com.dyzs.biometric.auth.Class2BiometricAuthPrompt;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_show_biometric).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LockScreenManager.showBiometricLock(this, "解锁", "请解锁", new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
    }
}