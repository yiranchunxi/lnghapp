package com.siasun.rtd.lngh.http.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.siasun.rtd.lngh.http.bean.ResponseMsgBean;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.ui.activity.LoginActivity;

import okhttp3.Call;


public class DecryptCallBack implements OnHttpListener<String> {

    private OnHttpListener mSource;
    private ChildrenCallBack childrenCallBack;
    public interface ChildrenCallBack{
        void onSucceed(String result);
        void onFail(Exception e);
    }
    public DecryptCallBack(OnHttpListener source,ChildrenCallBack childrenCallBack) {
        mSource=source;
        this.childrenCallBack=childrenCallBack;
    }

    @Override
    public void onStart(Call call) {
        if (mSource != null) {
            mSource.onStart(call);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSucceed(String result) {
        Log.e("test2","onSucceed");
        if(mSource!=null){
            ResponseMsgBean responseMsgBean = MsgHandler.msgDecode(result);
            if (responseMsgBean == null){
                childrenCallBack.onFail(new ApiException(Const.ERROR_DECODE_MSG_ERROR));
                return;
            } else if (!MsgHandler.checkMsgDi(responseMsgBean)){
                childrenCallBack.onFail(new ApiException(Const.ERROR_DECODE_MSG_ERROR,responseMsgBean.msg));
                return;
            }else{
                switch (responseMsgBean.cd){
                    case MsgHandler.ResponseCode.E_TOKEN_TIME_UP:

                        childrenCallBack.onFail(new ApiException(Const.ERROR_TOKEN_TIME_UP));
                        return;

                    case MsgHandler.ResponseCode.E_TOKEN_OTHER_DEV_LOGIN:
                        childrenCallBack.onFail(new ApiException(Const.ERROR_TOKEN_WRONG,responseMsgBean.msg));
                        return;

                    case MsgHandler.ResponseCode.NO_ERROR:
                        break;
                    default:
                        childrenCallBack.onFail(new ApiException(Const.ERROR_OTHER_WRONG));
                        return;
                }
                String responseStr = MsgHandler.decryptResponseMsg(responseMsgBean);
                Log.e("test2",responseStr);
                if (responseStr == null || responseStr.length() == 0){
                    childrenCallBack.onFail(new ApiException(Const.ERROR_CAN_NOT_DEC_DATA));
                    return;
                }
                if(childrenCallBack!=null){
                    childrenCallBack.onSucceed(responseStr);
                }
            }
        }
    }

    @Override
    public void onFail(Exception e) {
        if (mSource != null) {
            mSource.onFail(e);
            if(childrenCallBack!=null){
                childrenCallBack.onFail(e);
            }
        }
    }

    @Override
    public void onEnd(Call call) {
        if (mSource != null) {
            mSource.onEnd(call);
        }
    }

}
