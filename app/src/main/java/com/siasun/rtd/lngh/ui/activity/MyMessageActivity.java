package com.siasun.rtd.lngh.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.bean.MyMessageRequest;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.glide.GlideApp;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.MyMessageApi;
import com.siasun.rtd.lngh.http.request.QueryUserInfoApi;
import com.siasun.rtd.lngh.http.response.MyMessageResponse;
import com.siasun.rtd.lngh.http.response.QueryUserInfoResponseDTO;
import com.siasun.rtd.lngh.other.ClearInfoLogin;
import com.siasun.rtd.lngh.ui.adapter.MessageAdapter;
import com.siasun.rtd.lngh.ui.adapter.NewsAdapter;

public class MyMessageActivity extends MyActivity implements OnRefreshLoadMoreListener{

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private MessageAdapter mAdapter;
    private final  int NUMBER_PER_LOAD=10;
    @Override
    protected int getLayoutId() {
        return R.layout.my_message_activity;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        mRecyclerView = findViewById(R.id.rv_status_list);

        mAdapter = new MessageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
       queryMyMessage("");
    }
    public void queryMyMessage(String startId){
        MyMessageRequest bean=new MyMessageRequest();
        bean.token= Const.Tk;
        bean.start_id=startId;
        String requestString =  new Gson().toJson(bean);
        String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, true, MsgHandler.EncType.ENC_TYPE_2));

        EasyHttp.post(this)
                .api(new MyMessageApi()
                        .setRequestBody(requestMsg))

                .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                    @Override
                    public void onSucceed(String result) {
                        //toast(result);
                        MyMessageResponse response=new Gson().fromJson(result, MyMessageResponse.class);
                        if (response.msg_list.size() < NUMBER_PER_LOAD) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        }
                        if (TextUtils.isEmpty(startId)) {
                            mAdapter.clearData();
                            mAdapter.setData(response.msg_list);
                            mRefreshLayout.finishRefresh();
                            if(response.msg_list.size()==0){
                                toast("暂无消息");

                            }
                        } else {
                            mAdapter.addData(response.msg_list);
                            mRefreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        toast(e.getMessage());
                        if(e.toString().contains("系统检测到您的账号在其他设备登录")){
                            ClearInfoLogin.clearAndLogin(MyMessageActivity.this);
                            startActivity(LoginActivity.class);
                        }
                    }
                }));
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        toast("onLoadMore");
        String last_id=mAdapter.getData().get(mAdapter.getData().size()-1).id;
        queryMyMessage(last_id);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        toast("onRefresh");
        queryMyMessage("");
    }
    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyHttp.cancel(this);
    }
}
