package com.xiling.ddmall.shared.util;

import android.app.Activity;

import java.util.LinkedList;

/**
 * created by Jigsaw at 2018/10/10
 * 自己维护所有存在Activity栈的Activity
 */
public class ActivityController {
    private LinkedList<Activity> mActivityLinkedList = new LinkedList<>();

    private static ActivityController mActivityController;

    private ActivityController() {
    }

    public static ActivityController getInstance() {
        if (mActivityController == null) {
            synchronized (ActivityController.class) {
                if (mActivityController == null) {
                    mActivityController = new ActivityController();
                }
            }
        }

        return mActivityController;
    }

    public void pushActivity(Activity activity) {
        mActivityLinkedList.add(activity);
    }

    public void popActivity(Activity activity) {
        mActivityLinkedList.remove(activity);
    }

    public boolean isActivityAlive(Class activity) {
        if (activity == null) {
            return false;
        }
        for (Activity a : mActivityLinkedList) {
            if (activity.equals(a.getClass())) {
                return true;
            }
        }
        return false;
    }

}
