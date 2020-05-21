package com.yilian.mylibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
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
     * 根据类名获取
     * @param className
     * @return
     */
    public Activity getActivity(String className) {
        Stack<Activity> allActivity = getAllActivity();
        for (int i = 0; i < allActivity.size(); i++) {
            Activity activity = allActivity.get(i);
            if (activity.getClass().getSimpleName().equals(className)) {
                return activity;
            }

        }
        return null;

    }

    //	/**
//	 * 结束指定类名的Activity
//	 */
//	public void killActivity(Class<?> cls) {
//		Iterator<Activity> iterator = mActivityStack.iterator();
//
//		while(iterator.hasNext()){
//			Activity activity = iterator.next();
//			if (activity.getClass().equals(cls)) {
//
//				killActivity(activity);
//			}
//		}
//
//	}
    private static Stack<Activity> killActivityStack = new Stack<>();

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        Iterator<Activity> iterator = mActivityStack.iterator();

        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                killActivityStack.add(activity);
                activity.finish();
            }
        }
        mActivityStack.removeAll(killActivityStack);

    }

    /**
     * 结束所有Activity
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
     * 结束其他Activity
     */
    public void killOtherActivity(Activity activity) {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i) && activity != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    public Stack<Activity> getAllActivity() {
        return mActivityStack;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
