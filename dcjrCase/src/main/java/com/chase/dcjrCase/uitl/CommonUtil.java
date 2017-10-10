package com.chase.dcjrCase.uitl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.chase.dcjrCase.global.DCJRCaseApp;

public class CommonUtil {
	/**
	 * 在主线程中运行
	 * @param r
	 */
	public static void runOnUIThread(Runnable r){
		DCJRCaseApp.getHandler().post(r);
	}
	
	/**
	 * 获取resources对象
	 * @return
	 */
	public static Resources getResources(){
		return DCJRCaseApp.getContext().getResources();
	}
	
	/**
	 * 获取字符串
	 * @param resId
	 * @return
	 */
	public static String getString(int resId){
		return getResources().getString(resId);
	}

	/**
	 * 获取资源图片
	 * @param resId
	 * @return
     */
	public static Drawable getDrawable(int resId){
		return getResources().getDrawable(resId);
	}
	
	/**
	 * 获取dimes值
	 * @param resId
	 * @return
	 */
	public static float getDimens(int resId){
		return getResources().getDimension(resId);
	}
	
	/**
	 * 获取字符串的数组
	 * @param resId
	 * @return
	 */
	public static String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
	}
}