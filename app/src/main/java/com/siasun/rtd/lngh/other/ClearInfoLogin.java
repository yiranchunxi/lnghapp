package com.siasun.rtd.lngh.other;

import android.content.Context;

import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;

public class ClearInfoLogin {

    public static void clearAndLogin(Context context){

        Const.Tk="";
        SharedPreferenceUtil.getInstance().clearConfig(context, IntentKey.TOKEN);
        SharedPreferenceUtil.getInstance().clearConfig(context, IntentKey.PHONE);
        SharedPreferenceUtil.getInstance().clearConfig(context, "alias");
        SharedPreferenceUtil.getInstance().clearConfig(context, "identification");
        SharedPreferenceUtil.getInstance().clearConfig(context, "certificated");
        SharedPreferenceUtil.getInstance().clearConfig(context, "certificated");
        SharedPreferenceUtil.getInstance().clearConfig(context, "user_name");
        SharedPreferenceUtil.getInstance().clearConfig(context, "org_name");
        SharedPreferenceUtil.getInstance().clearConfig(context, "avatar");
        SharedPreferenceUtil.getInstance().clearConfig(context, "tag");
        SharedPreferenceUtil.getInstance().clearConfig(context, Const.AUTH_PSY);
        SharedPreferenceUtil.getInstance().clearConfig(context, Const.AUTH_LEGAL);
    }
}
