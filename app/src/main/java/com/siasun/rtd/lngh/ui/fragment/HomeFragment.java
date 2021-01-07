package com.siasun.rtd.lngh.ui.fragment;

import android.util.Log;

import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
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
        Log.e("test","initView");
    }

    @Override
    protected void initData() {
        //SharedPreferenceUtil.getInstance().put(getActivity(),"test","lngh");
        //Log.e("test",SharedPreferenceUtil.getInstance().get(getActivity(),"test"));
        //SharedPreferenceUtil.getInstance().clearConfig(getActivity(),"test");
        //Log.e("test",SharedPreferenceUtil.getInstance().get(getActivity(),"test"));
    }
}
