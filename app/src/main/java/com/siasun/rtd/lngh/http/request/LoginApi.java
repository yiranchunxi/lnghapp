package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

import okhttp3.RequestBody;

public final class LoginApi implements IRequestApi,IRequestType {


    @Override
    public String getApi() {
        return "user/login";
    }




    private String requestBody;

    public LoginApi setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
