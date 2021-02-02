package com.siasun.rtd.lngh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.action.StatusAction;
import com.siasun.rtd.lngh.aop.CheckNet;
import com.siasun.rtd.lngh.aop.DebugLog;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.PBOCDES;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.QueryUserIdApi;
import com.siasun.rtd.lngh.http.response.QueryUserIdResponse;
import com.siasun.rtd.lngh.other.ClearInfoLogin;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.dialog.MessageDialog;
import com.siasun.rtd.lngh.widget.BrowserView;
import com.siasun.rtd.lngh.widget.HintLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

public final class BrowserNoTitleBarActivity extends MyActivity implements StatusAction, OnRefreshListener {

    @CheckNet
    @DebugLog
    public static void start(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, BrowserNoTitleBarActivity.class);
        intent.putExtra(IntentKey.URL, url);
        context.startActivity(intent);
    }

    private HintLayout mHintLayout;
    private ProgressBar mProgressBar;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;
    private boolean isShow;

    @Override
    protected int getLayoutId() {
        return R.layout.browser_no_title_bar_activity;
    }


    @Override
    protected void initView() {
        mHintLayout = findViewById(R.id.hl_browser_hint);
        mProgressBar = findViewById(R.id.pb_browser_progress);
        mRefreshLayout = findViewById(R.id.sl_browser_refresh);
        mBrowserView = findViewById(R.id.wv_browser_view);

        // 设置网页刷新监听
        mRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void initData() {
        showLoading();

        mBrowserView.setBrowserViewClient(new MyBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new MyBrowserChromeClient(mBrowserView));

        String url = getString(IntentKey.URL);
        mBrowserView.loadUrl(url);
        EventBus.getDefault().register(this);
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }



    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBrowserView.canGoBack()) {
            // 后退网页并且拦截该事件
            mBrowserView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        isShow=true;
        mBrowserView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isShow=false;
        mBrowserView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBrowserView.onDestroy();
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
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
            mProgressBar.setProgress(newProgress);
        }
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_EXIT_SCENE)){
            if(isShow){
                finish();
            }
        }
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_EXIT_ROOT_SCENE)){
            finish();
        }
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_LOGIN_SCENE)){
            ClearInfoLogin.clearAndLogin(this);
            toast("请登录");
            startActivity(LoginActivity.class);
        }
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_LEGAL_AID_SCENE)){
            show_legal_aid_scene();
        }
    }

    private void show_legal_aid_scene() {

        showDialog();
        EasyHttp.post(this)
                .api(new QueryUserIdApi().setToken(Const.Tk))
                .request(new HttpCallback<QueryUserIdResponse>(this){
                    @Override
                    public void onSucceed(QueryUserIdResponse result) {
                        hideDialog();
                        if(result.result.equals("0")){
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                            StringBuilder signkey=new StringBuilder();
                            signkey.append(PBOCDES.pboc3DESEncryptForLongWithString("740a71055f36a56bea092475c65324f6",result.u_id));
                            signkey.append("6dcb1701-f6bb-47ae-9b64-abb46379784e");
                            signkey.append(sdf.format(new Date()));
                            StringBuilder builder=new StringBuilder("http://123.56.30.40:9997/ln/fotu?uid=");
                            builder.append(PBOCDES.pboc3DESEncryptForLongWithString("740a71055f36a56bea092475c65324f6",result.u_id));
                            builder.append("&signkey=");
                            builder.append(MD5Utils.MD5(signkey.toString()).toLowerCase());

                            Log.e("test",builder.toString());
                            if(TextUtils.isEmpty( SharedPreferenceUtil.getInstance().get(BrowserNoTitleBarActivity.this, Const.AUTH_LEGAL))) {
                                new MessageDialog.Builder(BrowserNoTitleBarActivity.this)
                                        // 标题可以不用填写
                                        .setTitle("授权管理")
                                        // 内容必须要填写
                                        .setMessage("法律援助请求获取以下权限:\n获得您的姓名、手机号")
                                        // 确定按钮文本
                                        .setConfirm(getString(R.string.auth_confirm))
                                        // 设置 null 表示不显示取消按钮
                                        .setCancel(getString(R.string.auth_refuse))
                                        // 设置点击按钮后不关闭对话框
                                        //.setAutoDismiss(false)
                                        .setListener(new MessageDialog.OnListener() {

                                            @Override
                                            public void onConfirm(BaseDialog dialog) {
                                                SharedPreferenceUtil.getInstance().put(BrowserNoTitleBarActivity.this, Const.AUTH_LEGAL, Const.AUTH_LEGAL);
                                                if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(BrowserNoTitleBarActivity.this, "user_name"))) {
                                                    builder.append("&name=");
                                                    try {
                                                        builder.append(URLEncoder.encode(SharedPreferenceUtil.getInstance().get(BrowserNoTitleBarActivity.this, "user_name"), "utf-8"));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                builder.append("&phone=");
                                                builder.append(result.phone_number);
                                                BrowserActivity.start(BrowserNoTitleBarActivity.this, builder.toString());
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                                BrowserActivity.start(BrowserNoTitleBarActivity.this, builder.toString());
                                            }
                                        })
                                        .show();
                            }else{
                                if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(BrowserNoTitleBarActivity.this, "user_name"))) {
                                    builder.append("&name=");
                                    try {
                                        builder.append(URLEncoder.encode(SharedPreferenceUtil.getInstance().get(BrowserNoTitleBarActivity.this, "user_name"), "utf-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                                builder.append("&phone=");
                                builder.append(result.phone_number);
                                BrowserActivity.start(BrowserNoTitleBarActivity.this, builder.toString());
                            }

                        }
                    }

                    @Override
                    public void onEnd(Call call) {
                        super.onEnd(call);
                        hideDialog();
                    }
                });
    }
}
