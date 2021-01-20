package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.siasun.rtd.lngh.http.server.InfoServer;

public final class SwiperApi  extends InfoServer implements IRequestApi, IRequestPath {


    @Override
    public String getApi() {
        return "service/swiper_list.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }
}
