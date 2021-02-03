package com.siasun.rtd.lngh.ui.activity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.ui.fragment.MyCollectFragment;
import com.siasun.rtd.lngh.ui.fragment.WebFragment;


public class MyCollectActivity extends MyActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.my_collect_activity;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tl_favorite_tab);
        mViewPager = findViewById(R.id.vp_favorite_pager);
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(MyCollectFragment.newInstance(), "资讯");
        mPagerAdapter.addFragment(WebFragment.newInstance(Const.COLLECT_PAGE_URL), "公会圈");
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }
}
