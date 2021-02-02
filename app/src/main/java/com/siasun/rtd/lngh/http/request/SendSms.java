package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class SendSms implements IRequestApi, IRequestType {

    @Override
    public String getApi() {
        return "sms/send_sms";
    }
    private String requestBody;

    public SendSms setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }


    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
