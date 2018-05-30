package com.dyzs.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ================================================
 * Created by dyzs on 2017/11/19.
 * <a href="dyzs.me@gmail.com">Contact me</a>
 * <a href="https://github.com/dyzs">Follow me</a>
 * ================================================
 * Description: 单例 toast
 */
public class ToastUtils {
	private ToastUtils() {
		throw new UnsupportedOperationException("toast util can't be initialized");
	}
	private static Toast toast;
	public static void makeText(Context context, String text){
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		toast.setText(text);
		toast.show();
	}

	public static void makeText(Context context, int resId){
		if (toast == null) {
			toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		toast.setText(resId);
		toast.show();
	}
}
