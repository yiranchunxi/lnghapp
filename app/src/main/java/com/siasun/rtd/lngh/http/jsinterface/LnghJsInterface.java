package com.siasun.rtd.lngh.http.jsinterface;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.prefs.Const;

import org.greenrobot.eventbus.EventBus;

public class LnghJsInterface {


    @JavascriptInterface
    public String getToken(String userName){
        Log.e("test","getToken");
        try {
            return Const.Tk.replace("\n", "");
        }catch (Exception e){

            return "";
        }
    }


    /**
     * 跳转到入会认证
     * @param data
     */
    @JavascriptInterface
    public void showMemberCertificationScene(String data){
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_MEMBER_CERTIFICATION_SCENE,"showMemberCertificationScene"));
    }

    /**
     * 打开一个网页
     * @param url
     * @param hideNavigationBar
     */
    @JavascriptInterface
    public void showWebScene(String url,boolean hideNavigationBar){
        // hideNavigationBar  true 显示header false不显示 header
        Log.e("test",url);
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_WEB_SCENE,url));
    }


    /**
     * 调用打电话
     * @param phoneNum
     */
    @JavascriptInterface
    public void showCallScene(String phoneNum){
        Log.e("test",phoneNum);
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_CALL_SCENE,phoneNum));
    }

    @JavascriptInterface
    public void exitScene(){
        Log.e("test","exitScene");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_EXIT_SCENE,""));
    }

    @JavascriptInterface
    public void exitRootScene(){
        Log.e("test","exitRootScene");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_EXIT_ROOT_SCENE,""));
    }

}
