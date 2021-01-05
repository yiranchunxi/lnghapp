package com.siasun.rtd.lngh.ui.activity;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;

public final class SplashActivity extends MyActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainTabActivity.class);
                finish();
            }
        },2000);

    }

    @Override
    protected void initData() {

    }
    @Override
    public boolean isSwipeEnable() {
        return false;
    }
    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }
}
