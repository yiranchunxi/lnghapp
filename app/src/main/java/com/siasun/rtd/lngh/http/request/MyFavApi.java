package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.siasun.rtd.lngh.http.server.InfoServer;

public class MyFavApi extends InfoServer implements IRequestApi, IRequestPath {


    @Override
    public String getApi() {
        return "service/news/my_fav.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }

    private String last_id;

    public MyFavApi setLast_id(String last_id) {
        this.last_id = last_id;
        return this;
    }
}
