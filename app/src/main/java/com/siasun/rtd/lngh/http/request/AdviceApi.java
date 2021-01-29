package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class AdviceApi implements IRequestApi, IRequestType , IRequestPath {
    @Override
    public String getApi() {
        return "service/advice/advice_submit.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }

    private String token;
    private String content;
    public AdviceApi setToken(String token) {
        this.token = token;
        return this;
    }

    public AdviceApi setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }
}
