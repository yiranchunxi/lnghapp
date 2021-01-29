package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class MyMessageApi implements IRequestApi, IRequestType {

    @Override
    public String getApi() {
        return "message/query_personal_message";
    }

    private String requestBody;

    public MyMessageApi setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
