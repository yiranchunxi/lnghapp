package com.siasun.rtd.lngh.ui.fragment;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.request.NewsApi;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;
import com.siasun.rtd.lngh.ui.adapter.NewsAdapter;

public class RecommendFragment extends MyFragment<MyActivity>
        implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private NewsAdapter mAdapter;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        mRecyclerView = findViewById(R.id.rv_status_list);

        mAdapter = new NewsAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this)
                .api(new NewsApi().setLast_id(""))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {

                        mAdapter.setData(result.data);
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        EasyHttp.get(this)
                .api(new NewsApi().setLast_id(""))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {
                        mAdapter.clearData();
                        mAdapter.setData(result.data);
                        mRefreshLayout.finishRefresh();
                        toast("刷新完成");
                    }
                });
    }
}
