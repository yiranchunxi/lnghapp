package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class QueryAccount implements IRequestApi, IRequestType {
    @Override
    public String getApi() {
        return "user/vf_account";
    }
    private String requestBody;

    public QueryAccount setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }


    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
