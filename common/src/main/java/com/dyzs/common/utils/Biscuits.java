package com.dyzs.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

		reflectionTN();
	}

	public void show() {
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.show();
	}

	/**
	 * 通过反射修改 toast 中的 tn
	 */
	private void reflectionTN() {
		try {
			//从Toast对象中获得mTN变量
			Field field = mToast.getClass().getDeclaredField("mTN");
			field.setAccessible(true);
			object = field.get(mToast);
			//TN对象中获得了show方法
			showMethod = object.getClass().getDeclaredMethod("show", null);
			hideMethod = object.getClass().getDeclaredMethod("hide", null);

			objectINotificationManager = object.getClass().getDeclaredMethod("getService");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object objectINotificationManager;
	private static Object object;
	private static Method showMethod, hideMethod;

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

	public void reflectionShow(long duration) {
		showReflectionToast();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				hideReflectionToast();
			}
		}, duration * 1000);
	}
}
