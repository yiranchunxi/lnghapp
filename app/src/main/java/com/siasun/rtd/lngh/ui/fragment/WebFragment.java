package com.siasun.rtd.lngh.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.action.StatusAction;
import com.siasun.rtd.lngh.aop.CheckNet;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.CertificationInfo;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.BrowserActivity;
import com.siasun.rtd.lngh.ui.activity.BrowserNoTitleBarActivity;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;
import com.siasun.rtd.lngh.widget.BrowserView;
import com.siasun.rtd.lngh.widget.HintLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 网页fragment
 */
public final class WebFragment extends MyFragment<MainTabActivity> implements StatusAction, OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private String url;
    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    private HintLayout mHintLayout;
    private ProgressBar mProgressBar;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;

    private int time = 0, count = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time += 1000;
            //toast(time);
            if (time > 10000) {
                if (mBrowserView != null) {

                    mProgressBar.setVisibility(View.GONE);
                    showComplete();
                    if (mBrowserView != null) {
                        mBrowserView.onPause();
                        mBrowserView.onResume();
                    }

                }
                handler.removeCallbacks(runnable);
            } else {
                handler.postDelayed(this, 1000);
            }

        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.web_fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            url= getArguments().getString(ARG_PARAM1);
        }
        mHintLayout = findViewById(R.id.hl_browser_hint);
        mProgressBar = findViewById(R.id.pb_browser_progress);
        mRefreshLayout = findViewById(R.id.sl_browser_refresh);
        mBrowserView = findViewById(R.id.wv_browser_view);

        // 设置网页刷新监听
        mRefreshLayout.setOnRefreshListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        showLoading();

        mBrowserView.setBrowserViewClient(new MyBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new MyBrowserChromeClient(mBrowserView));
        mBrowserView.loadUrl(url);
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }


    @Override
    public void onResume() {
        if(mBrowserView!=null){
            mBrowserView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mBrowserView!=null){
            mBrowserView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mBrowserView!=null){
            mBrowserView.onDestroy();
        }
        super.onDestroy();
    }



    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return false;
    }


    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        count = 0;
        time = 0;
        mBrowserView.reload();
    }

    private class MyBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(v -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            count = 0;
            time = 0;
            mProgressBar.setVisibility(View.GONE);
            mRefreshLayout.finishRefresh();
            showComplete();
        }
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        count = 0;
        time = 0;
        mBrowserView.reload();
    }


    private class MyBrowserChromeClient extends BrowserView.BrowserChromeClient {

        private MyBrowserChromeClient(BrowserView view) {
            super(view);
        }

        /**
         * 收到网页标题
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (title != null) {
                setTitle(title);
            }
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // 启动计时器
            if (newProgress > 50 && newProgress < 100) {
                if (count == 0) {
                    handler.postDelayed(runnable, 1000);
                    count++;
                }
            } else if (newProgress == 100) {
                // 停止计时器
                handler.removeCallbacks(runnable);
            }
            mProgressBar.setProgress(newProgress);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_WEB_SCENE)){
            BrowserNoTitleBarActivity.start(getAttachActivity(),messageEvent.getMessage());
        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_LOGIN_SCENE)){
            toast("请登录");
        }
    }


    @Override
    public void onDestroyView() {
        if(mBrowserView!=null){
            mBrowserView.onDestroy();
        }
        super.onDestroyView();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
