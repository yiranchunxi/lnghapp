package com.siasun.rtd.lngh.http.response;

import java.util.List;

/**
 * Created by zqc on 2018/1/18.
 */

public class QueryBannerResponseDTO<T> {
    public List<T> data;
    public List<T> act;
    public List<T> getData() {
        return data;
    }
    public List<T> getAct() {
        return act;
    }
}
