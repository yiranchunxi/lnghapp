package com.siasun.rtd.lngh.ui.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.glide.GlideApp;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.NewsApi;
import com.siasun.rtd.lngh.http.request.SwiperApi;
import com.siasun.rtd.lngh.http.response.QueryBannerResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryBannerResponseItemDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.BrowserActivity;
import com.siasun.rtd.lngh.ui.activity.BrowserNoTitleBarActivity;
import com.siasun.rtd.lngh.ui.adapter.NewsAdapter;
import com.siasun.rtd.lngh.widget.UPMarqueeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class RecommendFragment extends MyFragment<MyActivity>
        implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private NewsAdapter mAdapter;

    private BGABanner bgaBanner;

    private UPMarqueeView upMarView;

    List<View> views = new ArrayList<>();

    private TextView tv_home_service_more;
    private LinearLayout ll_service_hotline,ll_service_staff,ll_service_model,ll_service_sports;
    private static int NUMBER_PER_LOAD = 5;
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

        View headerView=mRecyclerView.addHeaderView(R.layout.views_news_header_banner);
        bgaBanner=headerView.findViewById(R.id.banner_guide_content);
        upMarView=headerView.findViewById(R.id.upMarView);

        tv_home_service_more=headerView.findViewById(R.id.tv_home_service_more);
        ll_service_hotline=headerView.findViewById(R.id.ll_service_hotline);
        ll_service_staff=headerView.findViewById(R.id.ll_service_staff);
        ll_service_model=headerView.findViewById(R.id.ll_service_model);
        ll_service_sports=headerView.findViewById(R.id.ll_service_sports);

        setOnClickListener(tv_home_service_more,ll_service_hotline,ll_service_staff,ll_service_model,ll_service_sports);


        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                GlideApp.with(getAttachActivity())
                        .load(model)
                        .into(itemView);
            }

        });




        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        EasyHttp.get(this)
                .api(new NewsApi().setLast_id(""))
                .request(new HttpCallback<QueryNewsResponseDTO<QueryNewsResponseItemDTO>>(this){
                    @Override
                    public void onSucceed(QueryNewsResponseDTO<QueryNewsResponseItemDTO> result) {
                        result.top_news.addAll(result.data);
                        mAdapter.setData(result.top_news);
                    }
                });


        getSwipeData();
    }
    @SingleClick
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_home_service_more:
                EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SHOW_SERVICE_SCENE,"event_tag_show_service_scene"));
                break;
            case R.id.ll_service_hotline:
                if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.TOKEN))){
                    BrowserNoTitleBarActivity.start(getAttachActivity(),Const.URL_LNGH_SERVER+"/lgh/views/12351_hotline/index.html");
                }else{
                    toast("请先登录!");
                }
                break;
            case R.id.ll_service_staff:
                if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.TOKEN))){
                    BrowserActivity.start(getAttachActivity(),"http://djk.chaoxing.com/index_5856.html");
                }else{
                    toast("请先登录!");
                }
                break;
            case R.id.ll_service_model:
                if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.TOKEN))){
                    BrowserNoTitleBarActivity.start(getAttachActivity(),Const.URL_LNGH_SERVER+"/lgh/views/workers_house/workers_searchO.html");
                }else{
                    toast("请先登录!");
                }
                break;
            case R.id.ll_service_sports:
                if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.TOKEN))){
                    BrowserNoTitleBarActivity.start(getAttachActivity(),Const.URL_LNGH_SERVER+"/lgh/views/activities_participate/sublist.html");
                }else{
                    toast("请先登录!");
                }
                break;
            default:
                break;
        }

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
                .api(new NewsApi().setLast_id(""))
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

        getSwipeData();
    }


    private void getSwipeData(){
        EasyHttp.get(this)
                .api(new SwiperApi())
                .request(new HttpCallback<QueryBannerResponseDTO<QueryBannerResponseItemDTO>>(this){

                    @Override
                    public void onSucceed(QueryBannerResponseDTO<QueryBannerResponseItemDTO> result) {
                        List<String> pic=new ArrayList<>();
                        List<String> title=new ArrayList<>();
                        for (QueryBannerResponseItemDTO queryBannerResponseItemDTO : result.getData()){
                            pic.add(queryBannerResponseItemDTO.pic_url);
                            title.add(queryBannerResponseItemDTO.pic_name);
                        }
                        bgaBanner.setData(pic, title);

                        bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                            @Override
                            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                                BrowserActivity.start(getAttachActivity(), result.getData().get(position).redirect_url);
                            }
                        });


                        views.clear();

                        for (QueryBannerResponseItemDTO queryBannerResponseItemDTO : result.getAct()){


                            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getAttachActivity()).inflate(R.layout.item_banner_view, null);
                            //初始化布局的控件

                            ImageView imageView=moreView.findViewById(R.id.iv_banner);

                            GlideApp.with(getAttachActivity())
                                    .load(queryBannerResponseItemDTO.pic_url)
                                    .into(imageView);

                            views.add(moreView);

                        }

                        upMarView.setViews(views);

                        upMarView.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, View view) {
                                BrowserActivity.start(getAttachActivity(), result.getAct().get(position).redirect_url);
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        toast(e.toString());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyHttp.cancel(this);
    }
}
