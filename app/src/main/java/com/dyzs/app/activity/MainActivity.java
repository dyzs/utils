package com.dyzs.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.app.presenter.MainPresenter;
import com.dyzs.app.service.CBMonitorService;
import com.dyzs.app.view.IMainView;
import com.dyzs.common.ui.FullScreenDialogVer2;
import com.dyzs.common.ui.LineChartView;
import com.dyzs.common.utils.FixDexUtils;
import com.dyzs.common.utils.ToastUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {
    MainPresenter presenter = new MainPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        final LineChartView lcv_yj = (LineChartView) findViewById(R.id.lcv_yj);
        lcv_yj.setData(lcv_yj.testLoadData(i));
        lcv_yj.setOnLineChartViewListener(new LineChartView.LineChartViewListener() {
            @Override
            public void onPointClick(int selection) {
                FixDexUtils.loadFixedDex(MainActivity.this, Environment.getExternalStorageDirectory());
                ToastUtils.makeText(MainActivity.this, "sel: " + selection);
            }
        });

        findViewById(R.id.magic_ruf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((MagicRUF) v).startTension(0);
                lcv_yj.setData(lcv_yj.testLoadData(i));
                lcv_yj.playLineAnimation();
            }
        });
    }
    private static int i = 11;

    @Override
    public void initData() {
        Intent intent = new Intent(this, CBMonitorService.class);
        startService(intent);
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
        FullScreenDialogVer2 dialogVer2 = new FullScreenDialogVer2.Builder(this)
                .setInterruptKeyEvent(true)
                .setContentView(view)
                .build();
        dialogVer2.show();

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
}
