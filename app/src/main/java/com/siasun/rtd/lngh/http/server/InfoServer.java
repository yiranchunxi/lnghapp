package com.siasun.rtd.lngh.http.server;

import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestServer;

public class InfoServer implements IRequestServer {
    @Override
    public String getHost() {
        IRequestServer server = EasyConfig.getInstance().getServer();
        /*if (server instanceof TestServer) {
            return "http://app.lnszgh.org/";
        }*/
        return "http://app.lnszgh.org/";
    }

    @Override
    public String getPath() {
        return "lngh_app/";
    }
}
