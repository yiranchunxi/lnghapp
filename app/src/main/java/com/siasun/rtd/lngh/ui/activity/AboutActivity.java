package com.siasun.rtd.lngh.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.siasun.rtd.lngh.BuildConfig;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.prefs.Const;

public class AboutActivity  extends MyActivity{

    private TextView tv_version,tv_deal,tv_pro;

    @Override
    protected int getLayoutId() {
        return R.layout.about_us_activity;
    }

    @Override
    protected void initView() {
        tv_version= (TextView) findViewById(R.id.tv_version);

        setOnClickListener(R.id.tv_deal,R.id.tv_pro);
    }

    @Override
    protected void initData() {
        tv_version.setText(BuildConfig.VERSION_NAME);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_deal:
                BrowserActivity.start(this, Const.PRIVACY_LISENCE_PAGE_URL);
                break;
            case R.id.tv_pro:
                BrowserActivity.start(this, Const.PRIVACY_PROTECT_PAGE_URL);
                break;
            default:
                break;
        }
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }
}
