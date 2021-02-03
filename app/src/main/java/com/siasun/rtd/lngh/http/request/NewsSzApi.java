package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.siasun.rtd.lngh.http.server.InfoServer;

public final class NewsSzApi extends InfoServer implements IRequestApi, IRequestPath {



    @Override
    public String getApi() {
        return "service/news_list_paged_shengz.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }

    private String last_id;

    public NewsSzApi setLast_id(String last_id) {
        this.last_id = last_id;
        return this;
    }
}
