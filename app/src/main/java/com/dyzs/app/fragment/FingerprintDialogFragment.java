/*
package com.dyzs.app.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyzs.app.R;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {
    private RelativeLayout fingerprintContainer;
    private TextView fingerprintDescription, fingerprintStatus;
    private ImageView fingerprintIcon;
    private RelativeLayout backupContainer;
    private FrameLayout description;
    private TextView passwordDescription, newFingerprintEnrolledDescription;
    private EditText password;
    private CheckBox useFingerprintInFutureCheck;
    private LinearLayout buttonPanel;
    private Button cancelButton, secondDialogButton;

    private FingerprintManager fingerprintManager;
    private CancellationSignal mCancellationSignal;
    private Cipher mCipher;
    private static final String DEFAULT_KEY_NAME = "default_key";
    private KeyStore keyStore;
    private Stage mStage = Stage.FINGERPRINT;
    private int flag = 5;           // 验证次数
    private int countdown = 1;      // 时间倒计时
    private boolean isSelfCancelled;// 标识是否是用户主动取消的认证

    // 是否支持指纹
    public boolean supportFingerprint() {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(getContext(), "您的系统版本过低，不支持指纹功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            KeyguardManager keyguardManager = getContext().getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getContext().getSystemService(FingerprintManager.class);
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    Toast.makeText(getContext(), "您的手机不支持指纹功能", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (keyguardManager != null) {
                    if (!keyguardManager.isKeyguardSecure()) {
                        Toast.makeText(getContext(), "您还未设置锁屏，请先设置锁屏并添加一个指纹", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        Toast.makeText(getContext(), "您至少需要在系统设置中添加一个指纹", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getContext(), "键盘管理初始化失败", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getContext(), "指纹管理初始化失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    // 初始化密钥库
    private void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 初始化密钥
    private void initCipher() {
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            setCipher(cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    // 向Activity传值的接口
    public interface FragmentInteraction {
        void onEditPassword(String strPassword);//输入的密码

        void onAuthenticated();//验证成功
    }

    private FragmentInteraction listener;

    private void initView(View view) {
        // 指纹登录
        fingerprintContainer = (RelativeLayout) view.findViewById(R.id.fingerprint_container);
        fingerprintDescription = (TextView) view.findViewById(R.id.fingerprint_description);
        fingerprintIcon = (ImageView) view.findViewById(R.id.fingerprint_icon);
        fingerprintStatus = (TextView) view.findViewById(R.id.fingerprint_status);
        // 密码登录
        backupContainer = (RelativeLayout) view.findViewById(R.id.backup_container);
        description = (FrameLayout) view.findViewById(R.id.description);
        passwordDescription = (TextView) view.findViewById(R.id.password_description);
        newFingerprintEnrolledDescription = (TextView) view.findViewById(R.id.new_fingerprint_enrolled_description);
        password = (EditText) view.findViewById(R.id.password);
        useFingerprintInFutureCheck = (CheckBox) view.findViewById(R.id.use_fingerprint_in_future_check);
        // 下方按钮
        buttonPanel = (LinearLayout) view.findViewById(R.id.buttonPanel);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        secondDialogButton = (Button) view.findViewById(R.id.second_dialog_button);

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    private void initEvent() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    stopListening();
                    dismiss();
                } else if (mStage == Stage.PASSWORD) {
                    password.setText("");
                    dismiss();
                }
            }
        });

        secondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    goToPassword();
                } else if (mStage == Stage.PASSWORD) {
                    //LogUtil( "输入密码是：" + password.getText().toString());
                    listener.onEditPassword(password.getText().toString());
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentInteraction) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        if (supportFingerprint()) {
            fingerprintManager = getContext().getSystemService(FingerprintManager.class);
            initKey();
            initCipher();
        } else {
            LogUtil("关闭dialog");
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("指纹验证");
        View v = inflater.inflate(R.layout.dialog_fingerprint, container, false);

        initView(v);
        initEvent();
        LogUtil("Fragment名字：" + getTag());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始指纹认证监听
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止指纹认证监听
        stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;//把传递进来的activity对象释放掉
    }

    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                if (!isSelfCancelled) {
                    fingerprintStatus.setText(errString);
                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                        //多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                        Message message = new Message();
                        message.what = -1;
                        message.obj = errString;
                        myHandler.sendMessage(message);
                    }
                }
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                fingerprintStatus.setText(helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                Message message = new Message();
                message.what = 1;
                myHandler.sendMessage(message);
            }

            @Override
            public void onAuthenticationFailed() {
                Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);
            }

        }, null);
    }

    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    LogUtil("handleMessage---多次失败");
                    */
/*if (flag == 0) {
                        // 传值失败时的系统时间，用来判断是否可以再次开启指纹
                        DateFormat dateFormat = DateFormat.getDateInstance(R.string.data_format);
                        listener.process(dateFormat.format(new Date(System.currentTimeMillis())));
                    }*//*

                    Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    goToPassword();
                    //dismiss();
                    break;

                case 1:
                    LogUtil("handleMessage---成功");
                    fingerprintIcon.setImageResource(R.drawable.ic_fingerprint_success);
                    fingerprintStatus.setText("成功");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.success_color));
                    fingerprintStatus.postDelayed(mSucceedFingerRunnable, countdown * 1000);
                    break;

                case 0:
                    flag--;
                    LogUtil("指纹识别失败，还剩下" + flag + "次机会");
                    fingerprintIcon.setImageResource(R.drawable.ic_fingerprint_error);
                    fingerprintStatus.setText("指纹识别失败，还剩下" + flag + "次机会");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.red));
                    fingerprintStatus.postDelayed(mResetFingerRunnable, countdown * 1000);
                    break;

                default:
                    break;
            }
        }
    };

    private Runnable mResetFingerRunnable = new Runnable() {
        @Override
        public void run() {
            fingerprintStatus.setText("重新验证");
            fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.blair_grey));
            fingerprintIcon.setImageResource(R.mipmap.icon_symbol_add);
        }
    };

    private Runnable mSucceedFingerRunnable = new Runnable() {
        @Override
        public void run() {
            listener.onAuthenticated();
            dismiss();
        }
    };

    private void goToPassword() {
        cancelButton.setText(R.string.action_settings);
        secondDialogButton.setText(R.string.action_settings);
        fingerprintContainer.setVisibility(View.GONE);
        backupContainer.setVisibility(View.VISIBLE);

        mStage = Stage.PASSWORD;
        password.requestFocus();
        stopListening();

    }

    private void LogUtil(String msg) {
        Log.e("指纹界面：", msg);
    }

    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}*/
