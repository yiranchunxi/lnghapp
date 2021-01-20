package com.siasun.rtd.lngh.http.prefs;


import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.siasun.rtd.lngh.http.bean.RequestMsgBean;
import com.siasun.rtd.lngh.http.bean.ResponseMsgBean;

import java.text.SimpleDateFormat;


/**
 * Created by zqc on 2017/7/17.
 */

public class MsgHandler {

    public static final byte[] a = {0x37, (byte)0xD3, (byte)0xC7, (byte)0xA0, (byte)0xBB, (byte)0xA3, 0x37, 0x12, (byte)0xB1, (byte)0xB6, (byte)0xAE, (byte)0xB9, (byte)0x9D, (byte)0x90, 0x61, (byte)0x88};

    public enum EncType{
        ENC_TYPE_1, ENC_TYPE_2
    }

    public interface ResponseCode{
        String NO_ERROR = "0";
        String E_TOKEN_TIME_UP = "E101";
        String E_TOKEN_OTHER_DEV_LOGIN = "E102";
        String E_OTHER = "E300";
    }

    public static String createRequestMsg(String requestData, boolean needTk, EncType encType){
        try {
            RequestMsgBean requestMsgBean = new RequestMsgBean();
            requestMsgBean.di = Const.DeviceId;
            if (needTk){
                requestMsgBean.tk = Const.Tk;
            }else {
                requestMsgBean.tk = "";
            }
            requestMsgBean.dt = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
            requestMsgBean.sig = MD5Utils.MD5(requestData);
            switch (encType){
                case ENC_TYPE_1:
                    requestMsgBean.et = "1";
                    requestMsgBean.rd = Base64.encodeToString(PBOCDES.pboc3DESEncryptForLong(PBOCDES.parseBytes(MD5Utils.MD5(Const.DeviceId + Const.LNGH)), requestData.getBytes(), true, true), Base64.NO_WRAP);
                    break;
                case ENC_TYPE_2:
                    requestMsgBean.et = "2";
                    requestMsgBean.rd = Base64.encodeToString(PBOCDES.pboc3DESEncryptForLong(PBOCDES.parseBytes(MD5Utils.MD5(Const.Tk + Const.LNGH)), requestData.getBytes(), true, true), Base64.NO_WRAP);
                    break;
            }
            return new Gson().toJson(requestMsgBean);
        }catch (Exception e){
            return "";
        }
    }


    public static String msgEncode(String requestData){
        String encryptStr = PBOCDES.hex(PBOCDES.pboc3DESEncryptForLong(a, requestData.getBytes(), true, true));
        return Base64.encodeToString(PBOCDES.parseBytes(encryptStr + MD5Utils.MD5(requestData)), Base64.NO_WRAP);
    }


    public static ResponseMsgBean msgDecode(String responseData){
        try {
            byte[] decodeByte = Base64.decode(responseData, Base64.NO_WRAP);
            if (responseData.length() < 32){
                return null;
            }
            if (decodeByte.length <16){
                return null;
            }
            byte[] encByte = new byte[decodeByte.length - 16];
            byte[] md5Byte = new byte[16];
            System.arraycopy(decodeByte, 0, encByte, 0, decodeByte.length - 16);
            System.arraycopy(decodeByte, decodeByte.length - 16, md5Byte, 0, 16);
            String decodeMsgStr = PBOCDES.pboc3DESDecryptWithByte(a, encByte);
            String md5Str = PBOCDES.hex(md5Byte);
            if (MD5Utils.MD5(decodeMsgStr).equalsIgnoreCase(md5Str)){
                return new Gson().fromJson(decodeMsgStr, ResponseMsgBean.class);
            }else {

                return null;
            }

        }catch (Exception e){
            Log.e("test1",e.toString());
            return null;
        }
    }

    public static String decryptResponseMsg(ResponseMsgBean responseMsgBean){
        try {
            String decryptData = "";
            switch (responseMsgBean.et){
                case "1":
                    decryptData = PBOCDES.pboc3DESDecryptWithByte(PBOCDES.parseBytes(MD5Utils.MD5(Const.DeviceId + Const.LNGH)), Base64.decode(responseMsgBean.rd, Base64.NO_WRAP));
                    break;
                case "2":
                    decryptData = PBOCDES.pboc3DESDecryptWithByte(PBOCDES.parseBytes(MD5Utils.MD5(Const.Tk + Const.LNGH)), Base64.decode(responseMsgBean.rd, Base64.NO_WRAP));
                    break;
            }
            if (MD5Utils.MD5(decryptData).equalsIgnoreCase(responseMsgBean.sig)){
                return decryptData;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static boolean checkMsgDi(ResponseMsgBean responseMsgBean){
        return Const.DeviceId.equalsIgnoreCase(responseMsgBean.di);
    }

    public static String decryptUploadResponseMsg(ResponseMsgBean responseMsgBean){
        try {
            String decryptData = "";
            decryptData = PBOCDES.pboc3DESDecryptWithByte(PBOCDES.parseBytes(MD5Utils.MD5(PBOCDES.hex(Base64.decode(Const.Tk, Base64.NO_WRAP)) + Const.LNGH)), Base64.decode(responseMsgBean.rd, Base64.NO_WRAP));

            if (MD5Utils.MD5(decryptData).equalsIgnoreCase(responseMsgBean.sig)){
                return decryptData;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
