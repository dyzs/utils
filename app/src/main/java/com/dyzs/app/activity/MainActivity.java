package com.dyzs.app.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyzs.aidl.IMyAidlInterface;
import com.dyzs.app.R;
import com.dyzs.app.presenter.MainPresenter;
import com.dyzs.app.view.IMainView;
import com.dyzs.base.BaseActivity;
import com.dyzs.common.examples.AnnotationExplainerImpl;
import com.dyzs.common.examples.IAnnotationExplainer;
import com.dyzs.common.ui.FullScreenDialogVer2;
import com.dyzs.common.ui.KnockBackupView;
import com.dyzs.common.ui.LineChartView;
import com.dyzs.common.ui.magicruf.MagicRUF;
import com.dyzs.common.utils.ColorUtils;
import com.dyzs.common.utils.LogUtils;
import com.dyzs.common.utils.ToastUtils;
import com.dyzs.testndk.AndroidNdkBridge;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {
    private static final String TAG = "MainActivity";
    MainPresenter presenter = new MainPresenter(this);

    @BindView(R.id.lcv_yj)
    LineChartView lcv_yj;

    @BindView(R.id.chart_redraw)
    TextView chart_redraw;
    @BindView(R.id.virgin_oil)
    KnockBackupView mBackupView;

    @BindView(R.id.layout_secret_base) RelativeLayout mLayoutSecretBase;
    @BindView(R.id.item_left) LinearLayout mItemLeft;
    @BindView(R.id.item_center) LinearLayout mItemCenter;
    @BindView(R.id.item_right) LinearLayout mItemRight;
    @BindView(R.id.item_cover) RelativeLayout mItemCover;
    @BindView(R.id.iv_symbol_add) ImageView mIvSymbolAdd;

    @BindView(R.id.tv_show_aidl_text) TextView mTvAidlText;
    @BindView(R.id.tv_show_ndk_text) TextView mTvNdkText;
    @BindView(R.id.rl_aidl_test) RelativeLayout rl_aidl_test;
    private IMyAidlInterface iMyAidlInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view)-> {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initReady();

        loadNdk();

        initAidl();

        annotationTest();
    }

    private void annotationTest() {
        IAnnotationExplainer iae = new AnnotationExplainerImpl();
        iae.valueOfGender("hybrid");
    }

    private void loadNdk() {
        AndroidNdkBridge bridge = new AndroidNdkBridge();
        mTvNdkText.setText(bridge.getString());
    }

    private void initAidl() {
        Intent intent = new Intent();
        intent.setAction("com.dyzs.aidl.AIDL_SERVER");// aidl server intent filter action
        intent.setPackage("com.dyzs.aidl");// aidl package name
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected");
                iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected");
            }
        }, BIND_AUTO_CREATE);
    }

    @Override
    public int initLayoutView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        lcv_yj.setData(lcv_yj.testLoadData(i));
        lcv_yj.setOnLineChartViewListener((selection)-> {
                // FixDexUtils.loadFixedDex(MainActivity.this, Environment.getExternalStorageDirectory());
                ToastUtils.makeText(MainActivity.this, "sel: " + selection);
        });

        findViewById(R.id.magic_ruf).setOnClickListener((v)-> {
                ((MagicRUF) v).startTension(0);
        });

        rl_aidl_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String pwd = iMyAidlInterface.getEncryptPwd();
                    mTvAidlText.setText(pwd);
                } catch (RemoteException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static int i = 11;

    @Override
    public void initData() {
        /*Intent intent = new Intent(this, CBMonitorService.class);
        startService(intent);*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_custom_view) {
            presenter.go2CustomView();
        } else if (id == R.id.nav_retrofit_sample) {
            presenter.go2RetrofitSample();
        } else if (id == R.id.nav_rx_permission) {
            presenter.go2RxPermission();
        } else if (id == R.id.nav_manage) {
            presenter.go2SampleHotfix();
        } else if (id == R.id.nav_permission) {
            presenter.go2AndroidPermission();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            showFullScreenDialogVer2();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showFullScreenDialogVer2 () {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_full, null);
        new FullScreenDialogVer2.Builder(this)
                .setInterruptKeyEvent(true)
                .setContentView(view)
                .build()
                .show();
        // dialogVer2.show();

        /*FullScreenDialog dialog = new FullScreenDialog(this);
        dialog.setContentView(view);
        dialog.show();*/
    }

    @Override
    public void go2CustomView() {
        Intent intent = new Intent(MainActivity.this, CustomViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void go2RetrofitSample() {
        Intent intent = new Intent(this, RetrofitSampleActivity.class);
        startActivity(intent);
    }

    @Override
    public void go2SampleHotfix() {
        /*Intent intent = new Intent(this, SampleHotfixActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void go2RxPermission() {
        Intent intent = new Intent(this, RxPermissionSampleActivity.class);
        startActivity(intent);
    }

    @Override
    public void go2AndroidPermissionRequest() {
        Intent intent = new Intent(this, PermissionRequestActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.chart_redraw, R.id.tv_start, R.id.tv_stop, R.id.item_center, R.id.item_left, R.id.item_right, R.id.item_cover})
    public void chartRedraw(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                /*mBackupView.setBackgroundColor(ContextCompat.getColor(this, R.color.maria_blue));
                mBackupView.setAllTexts(new ArrayList<>(Arrays.asList(KnockBackupView.TEXT2TEST)));
                mBackupView.startAnimation();*/
                handleShowAnim();
                break;
            case R.id.tv_stop:
                // mBackupView.cancelAnimator();
                // mBackupView.setBackgroundColor(Color.parseColor("#7FFF5E4D"));
                // mBackupView.setErrorStyle();
                // mBackupView.doneWithBackupAnim();
                handleHideAnim();
                break;
            case R.id.item_center:
                ToastUtils.makeText(this, "item center");
                break;
            case R.id.item_left:
                ToastUtils.makeText(this, "item left");
                break;
            case R.id.item_right:
                ToastUtils.makeText(this, "item right");
                break;
            case R.id.item_cover:
                ToastUtils.makeText(this, "item cover");
                handleHideAnim();
                break;
            default:

                break;
        }
    }

    private void initReady() {
        mLayoutSecretBase.setVisibility(View.INVISIBLE);
        mItemCover.setVisibility(View.INVISIBLE);
    }

    private boolean isShowing = false;
    private ValueAnimator mSecretBaseAnimator;
    private ValueAnimator mSymbolAddAnimator;
    private void handleShowAnim() {
        if (isShowing || (mSecretBaseAnimator != null && mSecretBaseAnimator.isRunning())) {
            return;
        }
        mItemCover.setVisibility(View.VISIBLE);
        mLayoutSecretBase.setVisibility(View.VISIBLE);
        int leftHeight = mItemLeft.getMeasuredHeight();
        int centerHeight = mItemCenter.getMeasuredHeight();
        int rightHeight = mItemRight.getMeasuredHeight();
        mItemLeft.setTranslationY(mItemLeft.getMeasuredHeight());
        mItemCenter.setTranslationY(mItemCenter.getMeasuredHeight());
        mItemRight.setTranslationY(mItemRight.getMeasuredHeight());

        mItemLeft.setAlpha(0f);
        mItemCenter.setAlpha(0f);
        mItemRight.setAlpha(0f);
        mItemCover.setBackgroundColor(Color.TRANSPARENT);

        mSecretBaseAnimator = ValueAnimator.ofInt(100, 0).setDuration(DURATION);
        mSecretBaseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSecretBaseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                float rateTransferValue = 1f * value / 100;
                LogUtils.v("animation", "value:"+ value + "//" + rateTransferValue);
                mItemLeft.setTranslationY(rateTransferValue * leftHeight);
                mItemCenter.setTranslationY(rateTransferValue * centerHeight);
                mItemRight.setTranslationY(rateTransferValue * rightHeight);

                float alpha = 1f - 1f * value / DURATION;

                mItemLeft.setAlpha(alpha);
                mItemCenter.setAlpha(alpha);
                mItemRight.setAlpha(alpha);
                mLayoutSecretBase.setAlpha(alpha);
                mItemCover.setBackgroundColor(
                        ColorUtils.getCompositeColorArgb(
                                ContextCompat.getColor(getApplicationContext(), R.color.transparent_black),
                                ContextCompat.getColor(getApplicationContext(), R.color.thirty_opacity_black),
                                alpha));
            }
        });
        mSecretBaseAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = true;
                mSecretBaseAnimator.removeAllListeners();
                mSecretBaseAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSecretBaseAnimator.start();

        mSymbolAddAnimator = ValueAnimator.ofInt(0, -135).setDuration(DURATION);
        mSymbolAddAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mSymbolAddAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            mIvSymbolAdd.setRotation(value);
        });
        mSymbolAddAnimator.start();
    }

    private static long DURATION = 300;
    private void handleHideAnim() {
        if (!isShowing || (mSecretBaseAnimator != null && mSecretBaseAnimator.isRunning())) {
            return;
        }
        int height = mLayoutSecretBase.getMeasuredHeight();
        mLayoutSecretBase.setVisibility(View.VISIBLE);

        mSecretBaseAnimator = ValueAnimator.ofInt(0, height * 3 / 2).setDuration(DURATION);
        mSecretBaseAnimator.setInterpolator(new AccelerateInterpolator());
        mSecretBaseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mItemLeft.setTranslationY(value);
                mItemCenter.setTranslationY(value);
                mItemRight.setTranslationY(value);

                float alpha = 1f - 1f * value / DURATION;

                LogUtils.v("animation", "value:" + alpha);

                if (alpha < 0f) alpha = 0f;
                mItemLeft.setAlpha(alpha);
                mItemCenter.setAlpha(alpha);
                mItemRight.setAlpha(alpha);
                mLayoutSecretBase.setAlpha(alpha);
                mItemCover.setBackgroundColor(
                        ColorUtils.getCompositeColorArgb(
                                ContextCompat.getColor(getApplicationContext(), R.color.transparent_black),
                                ContextCompat.getColor(getApplicationContext(), R.color.thirty_opacity_black),
                                alpha));
            }
        });
        mSecretBaseAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = false;
                mLayoutSecretBase.setVisibility(View.INVISIBLE);
                mItemCover.setVisibility(View.INVISIBLE);
                mSecretBaseAnimator.removeAllListeners();
                mSecretBaseAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSecretBaseAnimator.start();

        mSymbolAddAnimator = ValueAnimator.ofInt(-135, 0).setDuration(DURATION);
        mSymbolAddAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mSymbolAddAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            mIvSymbolAdd.setRotation(value);
        });
        mSymbolAddAnimator.start();
    }
}
