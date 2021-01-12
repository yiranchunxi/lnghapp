package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.hjq.http.model.BodyType;

public final class NewsApi implements IRequestApi, IRequestPath {



    @Override
    public String getApi() {
        return "service/news_list_paged.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }

    private String last_id;

    public NewsApi setLast_id(String last_id) {
        this.last_id = last_id;
        return this;
    }
}
