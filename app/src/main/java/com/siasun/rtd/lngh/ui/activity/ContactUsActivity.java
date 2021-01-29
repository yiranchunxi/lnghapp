package com.siasun.rtd.lngh.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.siasun.rtd.lngh.BuildConfig;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.prefs.Const;

public class ContactUsActivity extends MyActivity{



    @Override
    protected int getLayoutId() {
        return R.layout.contact_us_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

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
