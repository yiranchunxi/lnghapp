package com.siasun.rtd.lngh.ui.activity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.helper.ActivityStackManager;
import com.siasun.rtd.lngh.helper.DoubleClickHelper;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.other.KeyboardWatcher;
import com.siasun.rtd.lngh.ui.fragment.DiscoveryFragment;
import com.siasun.rtd.lngh.ui.fragment.HomeFragment;
import com.siasun.rtd.lngh.ui.fragment.MeFragment;
import com.siasun.rtd.lngh.ui.fragment.ServiceFragment;
import com.siasun.rtd.lngh.ui.fragment.UnionFragment;

import org.greenrobot.eventbus.EventBus;

public final class MainTabActivity  extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener  {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.main_tab_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_home_pager);
        mBottomNavigationView = findViewById(R.id.bv_home_navigation);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        KeyboardWatcher.with(this)
                .setListener(this);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(UnionFragment.newInstance());
        mPagerAdapter.addFragment(ServiceFragment.newInstance());
        mPagerAdapter.addFragment(DiscoveryFragment.newInstance());
        mPagerAdapter.addFragment(MeFragment.newInstance());

        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        mViewPager.setAdapter(mPagerAdapter);
    }


    /**
     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.menu_union:
                if(mViewPager.getCurrentItem()==1){
                    EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_CLICK_UNION,"event_tag_click_union"));
                }
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.menu_service:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.menu_discovery:
                mViewPager.setCurrentItem(3);
                return true;
            case R.id.menu_my:
                mViewPager.setCurrentItem(4);
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
        if (mPagerAdapter.getShowFragment().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {

                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }



}
