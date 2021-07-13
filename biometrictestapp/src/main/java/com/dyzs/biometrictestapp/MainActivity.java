package com.dyzs.biometrictestapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.dyzs.app.LockScreenManager;
import com.dyzs.biometric.BiometricPrompt;
import com.dyzs.biometric.CryptoObjectUtils;
import com.dyzs.biometric.auth.AuthPromptCallback;
import com.dyzs.biometric.auth.Class2BiometricAuthPrompt;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static String FINGERPRINT_BYTE_STR = "iC49/TmBVcUgBXj9q3kX6g==";
    public static String FACE_BYTE_STR = "k7URLaZppp/5sZqyxhlH9g==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_show_biometric).setOnClickListener(this);
        findViewById(R.id.tv_face_valid).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_show_biometric:
                showBiometric();
                break;
            case R.id.tv_face_valid:
                faceValid();
                break;
        }
    }

    private void faceValid() {
        try {
            /*Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            byte[] bytes = Base64.getDecoder().decode(FACE_BYTE_STR);
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES/CBC/PKCS7Padding");
            SecretKey secretkey = keygenerator.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            cipher.doFinal(bytes);
            BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);*/
            BiometricPrompt.CryptoObject cryptoObject = CryptoObjectUtils.createFakeCryptoObject();
            LockScreenManager.showBiometricLock2(this, cryptoObject, new AuthPromptCallback() {
                @Override
                public void onAuthenticationError(@Nullable FragmentActivity activity, int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(activity, errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@Nullable FragmentActivity activity, @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(activity, result);
                    BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
                    byte[] byteCipher = new byte[]{};
                    try {
                        byteCipher = cryptoObject.getCipher().doFinal();
                        String byteStr = Base64.getEncoder().encodeToString(byteCipher);
                        Log.i("TAG", "byte str:" + byteStr);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAuthenticationFailed(@Nullable FragmentActivity activity) {
                    super.onAuthenticationFailed(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBiometric() {
        LockScreenManager.showBiometricLock2(this, new AuthPromptCallback() {
            @Override
            public void onAuthenticationError(@Nullable FragmentActivity activity, int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(activity, errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@Nullable FragmentActivity activity, @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(activity, result);
                BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
                byte[] byteCipher = new byte[]{};
                try {
                    byteCipher = cryptoObject.getCipher().doFinal();
                    String byteStr = Base64.getEncoder().encodeToString(byteCipher);
                    Log.i("TAG", "byte str:" + byteStr);
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onAuthenticationFailed(@Nullable FragmentActivity activity) {
                super.onAuthenticationFailed(activity);
            }
        });
        /*LockScreenManager.showBiometricLock(this, "解锁", "请解锁", new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();

                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });*/
    }

}