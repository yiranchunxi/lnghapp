package com.siasun.rtd.lngh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.action.SwipeAction;
import com.siasun.rtd.lngh.aop.DebugLog;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.widget.PlayerView;

import java.io.File;

public class VideoPlayActivity extends MyActivity
        implements SwipeAction, PlayerView.onGoBackListener{

    public static void start(Context context, File file) {
        if (file == null || !file.isFile()) {
            return;
        }
        start(context, file.getPath(), file.getName());
    }

    @DebugLog
    public static void start(Context context, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(IntentKey.VIDEO, url);
        intent.putExtra(IntentKey.TITLE, title);
        context.startActivity(intent);
    }

    private PlayerView mPlayerView;

    @Override
    protected int getLayoutId() {
        return R.layout.video_play_activity;
    }

    @Override
    protected void initView() {
        mPlayerView = findViewById(R.id.pv_video_play_view);
        mPlayerView.setOnGoBackListener(this);
        mPlayerView.setGestureEnabled(true);
    }

    @Override
    protected void initData() {
        mPlayerView.setVideoTitle(getString(IntentKey.TITLE));
        mPlayerView.setVideoSource(getString(IntentKey.VIDEO));
        mPlayerView.start();
    }
    /**
     * {@link PlayerView.onGoBackListener}
     */
    @Override
    public void onClickGoBack(PlayerView view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        mPlayerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPlayerView.onDestroy();
        super.onDestroy();
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}
