package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class QueryUserInfoApi implements IRequestApi,IRequestType {

    @Override
    public String getApi() {
        return "user/query_user_info";
    }

    private String requestBody;

    public QueryUserInfoApi setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }
}
