package com.siasun.rtd.lngh.ui.fragment;

import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;

public final class HomeFragment extends MyFragment<MainTabActivity> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
