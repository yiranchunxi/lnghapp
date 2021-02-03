package com.siasun.rtd.lngh.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestPath;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

public class VideoApi implements IRequestServer , IRequestApi, IRequestPath {
    @Override
    public String getHost() {
        return "http://bs.lnszgh.org/";
    }

    @Override
    public String getPath() {
        return "video_thief/";
    }

    @Override
    public String getApi() {
        return "service/gh_video_list.php";
    }


    private int page;

    public VideoApi setPage(int page) {
        this.page = page;
        return this;
    }
}
