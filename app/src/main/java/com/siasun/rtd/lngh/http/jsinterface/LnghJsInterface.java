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
            Log.e("test",Const.Tk);
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

    @JavascriptInterface
    public void showWebScene(String url,boolean hideNavigationBar,String mid){
        // hideNavigationBar  true 显示header false不显示 header
        Log.e("test","showWebScene");
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
    /**
     * 关闭当前网页
     */
    @JavascriptInterface
    public void exitScene(){
        Log.e("test","exitScene");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_EXIT_SCENE,""));
    }

    /**
     * 关闭所有网页
     */
    @JavascriptInterface
    public void exitRootScene(){
        Log.e("test","exitRootScene");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_EXIT_ROOT_SCENE,""));
    }

    /**
     * 去登录
     */
    @JavascriptInterface
    public void showLoginScene(String msg){
        Log.e("test","showLoginScene");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_LOGIN_SCENE,""));
    }


    /**
     * 跳转心理咨询
     */
    @JavascriptInterface
    public void showPsyCounselingScene(){
        Log.e("test","showPsyCounselingScene()");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_PSYCOUNSELING_SCENE,""));
    }

    /**
     * 跳转法律援助
     */
    @JavascriptInterface
    public void showLegalAidScene(){
        Log.e("test","showLegalAidScene()");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_LEGAL_AID_SCENE,""));
    }

    /**
     * 点击跳转职工书屋
     */
    @JavascriptInterface
    public void showStaffBookstoreScene(){
        Log.e("test","showStaffBookstoreScene()");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_STAFF_BOOKSTORE_SCENE,""));
    }

    /**
     *
     */
    @JavascriptInterface
    public void showSendScene(){
        Log.e("test","showSendScene()");
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_SEND_SCENE,""));
    }

    /**
     * 工会圈消息id修改
     * @param mid
     */
    @JavascriptInterface
    public void newMsgId(String mid){
        EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_CIRCLE_POINT,mid));
    }
}
