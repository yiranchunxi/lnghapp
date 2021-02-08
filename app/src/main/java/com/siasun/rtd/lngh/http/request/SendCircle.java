package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class SendCircle implements IRequestApi, IRequestType {

    @Override
    public String getApi() {
        return "send_circle/send";
    }

    private String requestBody;

    public SendCircle setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.JSON;
    }


}
