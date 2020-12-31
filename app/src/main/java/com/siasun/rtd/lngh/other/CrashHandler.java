package com.siasun.rtd.lngh.other;

import android.app.Application;

import androidx.annotation.NonNull;



/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/02/03
 *    desc   : Crash 处理类
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 注册 Crash 监听
     */
    public static void register(Application application) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(application));
    }

    private Application mApplication;
    private Thread.UncaughtExceptionHandler mOldHandler;

    private CrashHandler(Application application) {
        mApplication = application;
        mOldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (getClass().getName().equals(mOldHandler.getClass().getName())) {
            // 请不要重复注册 Crash 监听
            throw new IllegalStateException("are you ok?");
        }
    }


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

    }
}