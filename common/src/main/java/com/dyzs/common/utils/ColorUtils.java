package com.dyzs.common.utils;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorUtils {
	/**
	 * 随机生成漂亮的颜色
	 * @return a newest random color
	 */
	public static int randomColor() {
		Random random = new Random();
		int red = random.nextInt(150) + 50;
		int green = random.nextInt(150) + 50;
		int blue = random.nextInt(150) + 50;
		return Color.rgb(red, green, blue);
	}

	/**
	 * 随机生成漂亮的颜色,带透明度的
	 * @return a newest random color argb
	 */
	public static int randomColorArgb() {
		Random random = new Random();
		int alpha = random.nextInt(70) + 30;
		int red = random.nextInt(150) + 50;
		int green = random.nextInt(150) + 50;
		int blue = random.nextInt(150) + 50;
		return Color.argb(alpha, red, green, blue);
	}


	/**
	 * 颜色与上一个十六进制数ARGB，得到一个颜色加深的效果，效果从 0-F 深
	 */
	public static int getColorDeeply(int color) {
		return color & 0xFFDDDDDD;
	}

	/**
     * 颜色值取反
     */
	public static int getColorReverse(int color) {
		// String string = String.format("#%x", color); // string reverse
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = 255 - Color.blue(color);
		return Color.argb(255, red, green, blue);
	}

	/**
	 * 获取两个颜色值之间渐变的某个点的颜色值, 不包含渐变
	 * @param resSColor
	 * @param resEColor
	 * @param rangeColorRate
	 * @return
	 */
	public static int getCompositeColor(int resSColor, int resEColor, float rangeColorRate) {
		int rS = Color.red(resSColor);
		int gS = Color.green(resSColor);
		int bS = Color.blue(resSColor);
		int rE = Color.red(resEColor);
		int gE = Color.green(resEColor);
		int bE = Color.blue(resEColor);
		int r = (int) (rS + (rE - rS) * 1f * rangeColorRate);
		int g = (int) (gS + (gE - gS) * 1f * rangeColorRate);
		int b = (int) (bS + (bE - bS) * 1f * rangeColorRate);
		return Color.argb(255, r, g, b);
	}

	public static int getCompositeColorArgb(int resSColor, int resEColor, float rangeColorRate) {
		int aS = Color.alpha(resSColor);
		int rS = Color.red(resSColor);
		int gS = Color.green(resSColor);
		int bS = Color.blue(resSColor);
		int aE = Color.alpha(resEColor);
		int rE = Color.red(resEColor);
		int gE = Color.green(resEColor);
		int bE = Color.blue(resEColor);
		int a = (int) (aS + (aE - aS) * 1f * rangeColorRate);
		int r = (int) (rS + (rE - rS) * 1f * rangeColorRate);
		int g = (int) (gS + (gE - gS) * 1f * rangeColorRate);
		int b = (int) (bS + (bE - bS) * 1f * rangeColorRate);
		return Color.argb(a, r, g, b);
	}

	/**
	 * Converts the given hex-color-string to rgb.
	 * 将给定的十六进制颜色字符串转换为RGB
	 */
	public static int rgb(String hex) {
		int color = (int) Long.parseLong(hex.replace("#", ""), 16);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		return Color.rgb(r, g, b);
	}

	/**
	 * turn an array of resource-colors (contains resource-id integers) into an
	 * array list of actual color integers
	 *
	 * @param r
	 * @param colors an integer array of resource id's of colors
	 * @return
	 */
	public static List<Integer> createColors(Resources r, int[] colors) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i : colors) {
			result.add(r.getColor(i));
		}
		return result;
	}

	/**
	 * Turns an array of colors (integer color values) into an ArrayList of
	 * colors.
	 *
	 * @param colors
	 * @return
	 */
	public static List<Integer> createColors(int[] colors) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i : colors) {
			result.add(i);
		}
		return result;
	}
}
