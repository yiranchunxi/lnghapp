package com.siasun.rtd.lngh.http.server;

public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return "http://app.lnszgh.org/";
    }

    @Override
    public String getPath() {
        return "lngh_app/";
    }
}
