package com.siasun.rtd.lngh.http.callback;


import com.siasun.rtd.lngh.http.prefs.Const;

/**
 * Created by Yan on 16/10/18.
 */
public class ApiException extends RuntimeException {


    private static String errorMsg;
    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }
    public ApiException(int resultCode, String detailMessage) {
        super(detailMessage);
        errorMsg=detailMessage;
    }
    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            case Const.ERROR_DECODE_MSG_ERROR:
            case Const.ERROR_OTHER_WRONG:
            case Const.ERROR_CAN_NOT_DEC_DATA:
                message = "服务器异常";
                break;
            case Const.ERROR_TOKEN_TIME_UP:
                message="连接超时" ;
                break;
            case Const.ERROR_TOKEN_WRONG:
                message ="请重新登录" ;
                break;
            default:
                message = "网络通信异常";
                break;

        }
        return message;
    }
}

