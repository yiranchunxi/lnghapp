package com.siasun.rtd.lngh.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
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
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.QueryUserIdApi;
import com.siasun.rtd.lngh.http.response.QueryUserIdResponse;
import com.siasun.rtd.lngh.other.ClearInfoLogin;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.BrowserActivity;
import com.siasun.rtd.lngh.ui.activity.BrowserNoTitleBarActivity;
import com.siasun.rtd.lngh.ui.activity.LoginActivity;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;
import com.siasun.rtd.lngh.ui.activity.SendCircleActivity;
import com.siasun.rtd.lngh.ui.dialog.MessageDialog;
import com.siasun.rtd.lngh.widget.BrowserView;
import com.siasun.rtd.lngh.widget.HintLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;

/**
 * 工会圈fragment
 */
public final class UnionFragment extends MyFragment<MainTabActivity> implements StatusAction{

    public static UnionFragment newInstance() {
        return new UnionFragment();
    }

    private HintLayout mHintLayout;
    private ProgressBar mProgressBar;
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
    private boolean isResume=false;

    @Override
    protected int getLayoutId() {
        return R.layout.union_fragment;
    }

    @Override
    protected void initView() {

        mHintLayout = findViewById(R.id.hl_browser_hint);
        mProgressBar = findViewById(R.id.pb_browser_progress);
        mBrowserView = findViewById(R.id.wv_browser_view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        showLoading();
        mBrowserView.setBrowserViewClient(new MyBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new MyBrowserChromeClient(mBrowserView));
        String url = "http://182.92.172.248/lgh/views/circle/circle.html";
        //String url = "http://app.lnszgh.org/lgh/views/service/service.html";
        mBrowserView.loadUrl(url);
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }


    @Override
    public void onResume() {
        if (mBrowserView != null) {
            mBrowserView.onResume();
        }
        isResume=true;
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mBrowserView != null) {
            mBrowserView.onPause();
        }
        isResume=false;
        super.onPause();
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
            //toast("onPageFinished");
            count = 0;
            time = 0;
            mProgressBar.setVisibility(View.GONE);
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
            //toast(newProgress);
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
            if(isResume){
                BrowserNoTitleBarActivity.start(getAttachActivity(),messageEvent.getMessage());
            }
        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_CALL_SCENE)){
            XXPermissions.with(getAttachActivity())
                    // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                    //.constantRequest()
                    // 支持请求6.0悬浮窗权限8.0请求安装权限
                    //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                    // 不指定权限则自动获取清单中的危险权限
                    .permission(Permission.CALL_PHONE)
                    .request(new OnPermission() {

                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            if (isAll) {
                                // toast("获取权限成功");
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                String phoneNum=messageEvent.getMessage();
                                if(phoneNum!=null&&!TextUtils.isEmpty(phoneNum)){
                                    Uri uri = Uri.parse("tel:" + phoneNum);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            } else {
                                toast("获取权限成功，部分权限未正常授予");
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                            if(quick) {
                                toast("被永久拒绝授权，请手动授予权限");
                                //如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(getAttachActivity());
                            } else {
                                toast("获取权限失败");
                            }
                        }
                    });


        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_LOGIN_SCENE)){
            if(isResume){
                ClearInfoLogin.clearAndLogin(getAttachActivity());
                toast("请登录");
                startActivity(LoginActivity.class);
            }
        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SUCCESS_LOGIN)){
            reload();
        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_CLICK_UNION)){
            reload();
        }else if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_SEND_SCENE)){
            startActivity(SendCircleActivity.class);
        }
    }



    @Override
    public void onDestroyView() {
        //toast("onDestroyView");
        if (mBrowserView != null) {
            mBrowserView.onDestroy();
        }
        super.onDestroyView();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        EasyHttp.cancel(this);
    }



}
