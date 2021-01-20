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


public class DecryptCallBack<T> implements OnHttpListener<T> {

    private OnHttpListener mSource;
    public DecryptCallBack(OnHttpListener source) {
        this.mSource=source;
    }

    @Override
    public void onStart(Call call) {
        if (mSource != null) {
            mSource.onStart(call);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSucceed(T result) {
        Log.e("test2","onSucceed");
        if(mSource!=null){
            ResponseMsgBean responseMsgBean = MsgHandler.msgDecode((String)result);
            if (responseMsgBean == null){
                mSource.onFail(new ApiException(Const.ERROR_DECODE_MSG_ERROR));
            } else if (!MsgHandler.checkMsgDi(responseMsgBean)){
                mSource.onFail(new ApiException(Const.ERROR_DECODE_MSG_ERROR));
            }else{
                switch (responseMsgBean.cd){
                    case MsgHandler.ResponseCode.E_TOKEN_TIME_UP:

                        mSource.onFail(new ApiException(Const.ERROR_TOKEN_TIME_UP));
                    case MsgHandler.ResponseCode.E_TOKEN_OTHER_DEV_LOGIN:
                        mSource.onFail(new ApiException(Const.ERROR_TOKEN_WRONG));
                    case MsgHandler.ResponseCode.E_OTHER:
                        mSource.onFail(new ApiException(Const.ERROR_OTHER_WRONG));
                    case MsgHandler.ResponseCode.NO_ERROR:
                        break;
                    default:
                        mSource.onFail(new ApiException(Const.ERROR_OTHER_WRONG));
                }
                String responseStr = MsgHandler.decryptResponseMsg(responseMsgBean);
                Log.e("test2",responseStr);
                mSource.onSucceed(responseStr);
            }
        }
    }

    @Override
    public void onFail(Exception e) {
        if (mSource != null) {
            mSource.onFail(e);
        }
    }

    @Override
    public void onEnd(Call call) {
        if (mSource != null) {
            mSource.onEnd(call);
        }
    }

}
