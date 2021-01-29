package com.siasun.rtd.lngh.receiver;

import android.content.Context;
import android.text.TextUtils;

import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;

import java.util.HashSet;
import java.util.Set;



public class PushConfig {

    public static void startPush(Context context,String alias){
        /*JPushInterface.resumePush(context);
        JPushInterface.setAlias(context, Const.JPUSH_SET_ALIAS,alias);
        JPushInterface.cleanTags(context,Const.JPUSH_DELETE_ALIAS);
        Set set=new HashSet();
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"tag"))){
            set.add(SharedPreferenceUtil.getInstance().get(context,"tag"));
        }
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p1"))||SharedPreferenceUtil.getInstance().get(context,"p1").equals("p1"))
        {
            set.add("p1");
        }
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p2"))||SharedPreferenceUtil.getInstance().get(context,"p2").equals("p2")){
            set.add("p2");
        }
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p3"))||SharedPreferenceUtil.getInstance().get(context,"p3").equals("p3")){
            set.add("p3");
        }


        JPushInterface.setTags(context,Const.JPUSH_SET_TAGS,set);*/
    }


    public static void stopPush(Context context){
        /*JPushInterface.deleteAlias(context,Const.JPUSH_DELETE_ALIAS);
        JPushInterface.cleanTags(context,Const.JPUSH_DELETE_ALIAS);
        Set set=new HashSet();
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p1"))||SharedPreferenceUtil.getInstance().get(context,"p1").equals("p1"))
        {
            set.add("p1");
        }
        JPushInterface.setTags(context,Const.JPUSH_SET_TAGS,set);*/
    }

}
