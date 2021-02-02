package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class VerifyRegister implements IRequestApi, IRequestType {

    @Override
    public String getApi() {
        return "user/verify_register_sms_code";
    }

    private String requestBody;

    public VerifyRegister setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
