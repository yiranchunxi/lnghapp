package com.siasun.rtd.lngh.ui.fragment;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.VideoBean;
import com.siasun.rtd.lngh.http.request.VideoApi;
import com.siasun.rtd.lngh.http.response.VideoResponse;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;
import com.siasun.rtd.lngh.ui.activity.VideoPlayActivity;
import com.siasun.rtd.lngh.ui.adapter.NewsAdapter;
import com.siasun.rtd.lngh.ui.adapter.VideoAdapter;

public class VideoFragment extends MyFragment<MainTabActivity> implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener{

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private VideoAdapter mAdapter;
    private static int NUMBER_PER_LOAD = 10;
    private static int PAGE = 0;
    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_fragment;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        mRecyclerView = findViewById(R.id.rv_status_list);

        mAdapter = new VideoAdapter(getAttachActivity());
        mAdapter.setOnChildClickListener(R.id.rl_video_container, new BaseAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
                VideoPlayActivity.start(getAttachActivity(), mAdapter.getItem(position).video_push, mAdapter.getItem(position).title);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this)
                .api(new VideoApi().setPage(PAGE))
                .request(new HttpCallback<VideoResponse>(this){
                    @Override
                    public void onSucceed(VideoResponse result) {
                        mAdapter.setData(result.result);
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        showDialog();
        PAGE++;
        EasyHttp.get(this)
                .api(new VideoApi().setPage(PAGE))
                .request(new HttpCallback<VideoResponse>(this){
                    @Override
                    public void onSucceed(VideoResponse result) {
                        hideDialog();
                        if (result.result.size() < NUMBER_PER_LOAD) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }

                        mAdapter.addData(result.result);
                        mRefreshLayout.finishLoadMore();

                    }

                    @Override
                    public void onFail(Exception e) {
                        hideDialog();
                        toast(e.getMessage());
                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        PAGE=0;
        EasyHttp.get(this)
                .api(new VideoApi().setPage(PAGE))
                .request(new HttpCallback<VideoResponse>(this){
                    @Override
                    public void onSucceed(VideoResponse result) {
                        mAdapter.clearData();
                        mAdapter.setData(result.result);
                        mRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFail(Exception e) {
                        hideDialog();
                        toast(e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyHttp.cancel(this);
    }
}
