package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;

public final class SwiperApi implements IRequestApi, IRequestPath {


    @Override
    public String getApi() {
        return "service/swiper_list.php";
    }

    @Override
    public String getPath() {
        return "szgh_cms/";
    }
}
