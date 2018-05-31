package com.dyzs.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dyzs.common.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ================================================
 * Created by dyzs on 2018/05/30.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 * see blog:https://www.cnblogs.com/net168/p/4058193.html
 */
public class Biscuits {
	private Biscuits() {
		throw new UnsupportedOperationException("biscuits can't be initialized");
	}
	private static Biscuits instance;
	public static Biscuits getInstance(Context context) {
		if (instance == null) {
			synchronized (Biscuits.class) {
				if (instance == null) {
					instance = new Biscuits(context);
				}
			}
		}
		return instance;
	}
	private static Toast mToast;
	private Biscuits(Context context) {
		mToast = new Toast(context.getApplicationContext());
		View v = LayoutInflater.from(context).inflate(R.layout.layout_biscuits, null);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setView(v);

		mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		// params.windowAnimations = com.android.internal.R.style.Animation_Toast;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

		mWM.addView(v, params);

		/* Deprecated */
		reflectionTN();
	}

	private WindowManager mWM;
	private WindowManager.LayoutParams params;

	public void show() {
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.show();
	}

	/**
	 * 通过反射修改 toast 中的 tn
	 */
	@Deprecated
	private void reflectionTN() {
		try {
			//从Toast对象中获得mTN变量
			Field field = mToast.getClass().getDeclaredField("mTN");
			field.setAccessible(true);
			object = field.get(mToast);
			//TN对象中获得了show方法
			showMethod = object.getClass().getDeclaredMethod("show", null);
			hideMethod = object.getClass().getDeclaredMethod("hide", null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object object;
	private Method showMethod, hideMethod;

	private void showReflectionToast() {
		try {
			//调用show方法来显示Toast信息提示框
			showMethod.invoke(object, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void hideReflectionToast(){
		try{
			hideMethod.invoke(object, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void reflectionShow(long seconds) {
		showReflectionToast();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				hideReflectionToast();
			}
		}, seconds * 1000);
	}
}
