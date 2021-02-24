package com.siasun.rtd.lngh.manager;

import com.siasun.rtd.lngh.helper.ActivityStackManager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class AppThreadManger extends ThreadPoolExecutor {
    private static  volatile  AppThreadManger sInstance;

    public AppThreadManger(){
        super(1,3,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    }

    public static AppThreadManger getsInstance() {
        if(sInstance==null){
            synchronized (ActivityStackManager.class){
                if(sInstance==null){
                    sInstance=new AppThreadManger();
                }
            }
        }

        return sInstance;
    }
}
