package com.siasun.rtd.lngh.ui.fragment;

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
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.request.MyFavApi;
import com.siasun.rtd.lngh.http.request.NewsApi;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;
import com.siasun.rtd.lngh.ui.activity.BrowserActivity;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;
import com.siasun.rtd.lngh.ui.activity.MyCollectActivity;
import com.siasun.rtd.lngh.ui.adapter.NewsAdapter;

/**
 * 我的收藏fragment
 */
public final class MyCollectFragment extends MyFragment<MyCollectActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener {
    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private NewsAdapter mAdapter;
    private static int NUMBER_PER_LOAD = 5;
    public static MyCollectFragment newInstance() {
        return new MyCollectFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_collect_fragment;
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
                .api(new MyFavApi().setLast_id(""))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {
                        result.top_news.addAll(result.data);
                        mAdapter.setData(result.top_news);
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        BrowserActivity.start(getAttachActivity(), mAdapter.getData().get(position).redirect_url);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        String last_id=mAdapter.getData().get(mAdapter.getData().size()-1).id;
        //toast(last_id);

        EasyHttp.get(this)
                .api(new NewsApi().setLast_id(last_id))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {
                        if (result.data.size() < NUMBER_PER_LOAD) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                        mAdapter.addData(result.data);
                        mRefreshLayout.finishLoadMore();

                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        EasyHttp.get(this)
                .api(new MyFavApi().setLast_id(""))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {
                        mAdapter.clearData();
                        result.top_news.addAll(result.data);
                        mAdapter.setData(result.top_news);
                        mRefreshLayout.finishRefresh();
                        //toast("刷新完成");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        EasyHttp.cancel(this);
        super.onDestroyView();
    }
}
