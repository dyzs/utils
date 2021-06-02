package com.dyzs.app;

import android.os.Build;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dyzs.biometric.BiometricPrompt;
import com.dyzs.biometric.auth.AuthPrompt;
import com.dyzs.biometric.auth.AuthPromptCallback;
import com.dyzs.biometric.auth.AuthPromptHost;
import com.dyzs.biometric.auth.Class3BiometricAuthPrompt;

import java.util.concurrent.Executor;

public class LockScreenManager {
    static class InnerExecutor implements Executor {
        Handler handler;

        public InnerExecutor(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }

    public static void showBiometricLock(FragmentActivity activity, String title, String description, BiometricPrompt.AuthenticationCallback callback) {
        Executor executor = Build.VERSION.SDK_INT < 28 ? new InnerExecutor(new Handler(activity.getMainLooper())) : activity.getMainExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, callback);
        showBiometricLock(biometricPrompt, title, description);
    }

    public static void showBiometricLock(Fragment fragment, String title, String description, BiometricPrompt.AuthenticationCallback callback) {
        Executor executor = Build.VERSION.SDK_INT < 28 ? new InnerExecutor(new Handler(fragment.getActivity().getMainLooper())) : fragment.getActivity().getMainExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(fragment, executor, callback);
        showBiometricLock(biometricPrompt, title, description);
    }

    public static void showBiometricLock(BiometricPrompt biometricPrompt, String title, String description) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title) //设置大标题
                .setSubtitle(description) // 设置标题下的提示
                .setDeviceCredentialAllowed(true)
                .setConfirmationRequired(false)
//                .setAllowedAuthenticators()
//                .setNegativeButtonText("取消") //设置取消按钮
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    public static void showBiometricLock2(FragmentActivity activity, AuthPromptCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("title") //设置大标题
                .setSubtitle("description") // 设置标题下的提示
                .setDeviceCredentialAllowed(true)
                .setConfirmationRequired(false)
//                .setAllowedAuthenticators()
//                .setNegativeButtonText("取消") //设置取消按钮
                .build();
        Class3BiometricAuthPrompt class3BiometricAuthPrompt = new Class3BiometricAuthPrompt(promptInfo);
        class3BiometricAuthPrompt.startAuthentication(new AuthPromptHost(activity), null, callback);
    }
}
