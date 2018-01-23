package com.dyzs.app.test.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.dyzs.app.R;
import com.dyzs.app.utils.LogUtils;

/**
 * Created by NKlaus on 2017/11/26.
 */

public class ClockWidget extends AppWidgetProvider{
    private static String TAG = ClockWidget.class.getSimpleName();
    private AppWidgetManager mAppWidgetManager;
    /*public ClockWidget() {
        super();
        LogUtils.i(TAG, "constructor.....");
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogUtils.i(TAG, "on receive.....");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        LogUtils.i(TAG, "on update.....");
        ComponentName provider = new ComponentName(context, ClockWidget.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock);
        Intent intent = new Intent();
        intent.setAction("com.dyzs.nurse.widget");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // views.setOnClickPendingIntent(R.id.ids, pendingIntent);

        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetManager.updateAppWidget(provider, views);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        LogUtils.i(TAG, "on app widget options changed.....");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        LogUtils.i(TAG, "on deleted.....");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtils.i(TAG, "on enabled.....");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        LogUtils.i(TAG, "on disabled.....");
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
