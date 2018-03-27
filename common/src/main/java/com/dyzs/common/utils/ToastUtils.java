package com.dyzs.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by maidou on 2015/3/24.
 * 单例模式的 Toast
 */
public class ToastUtils {
	private ToastUtils() {
		throw new UnsupportedOperationException("toast util can't be initialized");
	}
	private static Toast toast;
	public static void makeText(Context context, String text){
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}

	public static void makeText(Context context, int resId){
		if (toast == null) {
			toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		}
		toast.setText(resId);
		toast.show();
	}
}
