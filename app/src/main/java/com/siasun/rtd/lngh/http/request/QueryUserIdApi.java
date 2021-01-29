package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class QueryUserIdApi implements IRequestApi,IRequestType {

    @Override
    public String getApi() {
        return "server/query_user_id";
    }

    private String token;

    public QueryUserIdApi setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }
}
