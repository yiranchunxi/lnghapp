package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class SubmitRegister implements IRequestApi, IRequestType {

    @Override
    public String getApi() {
        return "user/submit_register";
    }

    private String requestBody;

    public SubmitRegister setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
