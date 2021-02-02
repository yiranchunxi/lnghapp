package com.siasun.rtd.lngh.http.server;

public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return "http://182.92.172.248:8000/";
       //return "http://192.168.8.121:8000/";
       // return "http://app.lnszgh.org/";
    }

    @Override
    public String getPath() {
        return "lngh_app/";
    }
}
