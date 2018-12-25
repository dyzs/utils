package com.dyzs.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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
public class ToastUtilsVer2 {
	private ToastUtilsVer2() {
		throw new UnsupportedOperationException("biscuits can't be initialized");
	}

	private static Toast mToast;
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
			showMethod = object.getClass().getDeclaredMethod("show", (Class<?>) null);
			hideMethod = object.getClass().getDeclaredMethod("hide", (Class<?>) null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object object;
	private Method showMethod, hideMethod;

	private void showReflectionToast() {
		try {
			//调用show方法来显示Toast信息提示框
			showMethod.invoke(object, (Object) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void hideReflectionToast(){
		try{
			hideMethod.invoke(object, (Object) null);
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
	private TextView textView;
	private ToastUtilsVer2(Builder builder) {
		View v = LayoutInflater.from(builder.context).inflate(R.layout.layout_toast_ver2, null);
		textView = (TextView) v.findViewById(R.id.text);
		if (mToast == null)mToast = new Toast(builder.context);
		mToast.setView(v);
		setText(builder.text);
		setTextColor(builder.color);
		setGravity(builder.gravity, builder.xOffset, builder.yOffset);
		setDuration(builder.duration);
		/* Deprecated */
		reflectionTN();
	}

	private void setText(String text) {
		if (textView != null)textView.setText(text);
	}

	private void setTextColor(int color) {
		if (textView != null)textView.setTextColor(color);
	}

	private void setGravity(int gravity, int xOffset, int yOffset) {
		mToast.setGravity(gravity, xOffset, yOffset);
	}

	private void setDuration(int duration) {
		mToast.setDuration(duration);
	}

	public static class Builder {
		protected Context context;
		protected String text;
		protected int color;
		protected int gravity;
		private int xOffset;
		private int yOffset;
		protected int duration;
		public Builder(Context context) {
			this.context = context;
			this.text = "toast";
			this.color = Color.BLACK;
			this.gravity = Gravity.CENTER;
			this.xOffset = 0;
			this.yOffset = 0;
			this.duration = Toast.LENGTH_SHORT;
		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public Builder setTextColor(int color) {
			this.color = color;
			return this;
		}

		public Builder setGravity(int gravity, int xOffset, int yOffset) {
			this.gravity = gravity;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			return this;
		}

		public Builder setDuration(int duration) {
			this.duration = duration;
			return this;
		}

		public ToastUtilsVer2 build() {
			return new ToastUtilsVer2(this);
		}

		public ToastUtilsVer2 show() {
			ToastUtilsVer2 biscuits = build();
			biscuits.show();
			return biscuits;
		}
	}
}
