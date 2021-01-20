package com.siasun.rtd.lngh.ui.fragment;

import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.request.NewsApi;
import com.siasun.rtd.lngh.http.request.SwiperApi;
import com.siasun.rtd.lngh.http.response.QueryBannerResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryBannerResponseItemDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseDTO;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;

/**
 * 首页Fragment
 */
public final class HomeFragment extends MyFragment<MainTabActivity> {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {

        mTabLayout = findViewById(R.id.tl_home_tab);
        mViewPager = findViewById(R.id.vp_home_pager);
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(RecommendFragment.newInstance(), "推荐");
        mPagerAdapter.addFragment(UnionFragment.newInstance(), "省总");
        mPagerAdapter.addFragment(UnionFragment.newInstance(), "市总");
        mPagerAdapter.addFragment(UnionFragment.newInstance(), "视频");
        mPagerAdapter.addFragment(UnionFragment.newInstance(), "关于我们");
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        //SharedPreferenceUtil.getInstance().put(getActivity(),"test","lngh");
        //Log.e("test",SharedPreferenceUtil.getInstance().get(getActivity(),"test"));
        //SharedPreferenceUtil.getInstance().clearConfig(getActivity(),"test");
        //Log.e("test",SharedPreferenceUtil.getInstance().get(getActivity(),"test"));



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
}
