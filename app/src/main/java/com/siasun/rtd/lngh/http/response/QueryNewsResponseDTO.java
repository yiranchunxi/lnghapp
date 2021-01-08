package com.siasun.rtd.lngh.http.response;

import java.util.List;

/**
 * Created by zqc on 2018/1/18.
 */

public class QueryNewsResponseDTO<T> {

    public List<T> data;
    public List<T> top_news;
}
