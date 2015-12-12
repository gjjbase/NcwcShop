package com.ncwc.shop.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.ncwc.shop.base.AppManager;

import java.util.Stack;

/**
 * Created by DELL-PC on 2015/12/7.
 */
public class AppManagerTwo {

	private static Stack<Activity> mActivityStack;
	private static AppManagerTwo mAppManager;

	/**
	 * 单一实例
	 */
	public static AppManagerTwo getInstance() {
		if (mAppManager == null) {
			mAppManager = new AppManagerTwo();
		}
		return mAppManager;
	}

	/**
	 * 添加Activity到堆
	 */
	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取栈顶Activity
	 */
	public Activity getTopActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 结束栈顶Activity
	 */
	public void killTopActivity() {
		Activity activity = mActivityStack.lastElement();
		killActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void killActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void killActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				killActivity(activity);
			}
		}
	}

	/**
	 * 结束Activity
	 */
	public void killAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			killAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

}
