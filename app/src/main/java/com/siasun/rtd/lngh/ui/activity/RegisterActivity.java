package com.siasun.rtd.lngh.ui.activity;

import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.widget.layout.NoScrollViewPager;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.ui.fragment.RegisterPasswordFragment;
import com.siasun.rtd.lngh.ui.fragment.RegisterPhoneFragment;
import com.siasun.rtd.lngh.ui.fragment.RegisterVerifyFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册界面
 */
public class RegisterActivity extends MyActivity {
    private NoScrollViewPager myViewPager;

    private int current_postion;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.register_activity;
    }

    @Override
    protected void initView() {
        myViewPager=findViewById(R.id.vp_register_pager);

        setOnClickListener(R.id.actionBarBackButton,R.id.actionBarCloseButton);
        mPagerAdapter=new BaseFragmentAdapter<MyFragment>(this);

        mPagerAdapter.addFragment(RegisterPhoneFragment.newInstance());
        mPagerAdapter.addFragment(RegisterVerifyFragment.newInstance());
        mPagerAdapter.addFragment(RegisterPasswordFragment.newInstance());

        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        //myViewPager.setOffscreenPageLimit(2);
        myViewPager.setAdapter(mPagerAdapter);


    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actionBarCloseButton:
                finish();
                break;

            case R.id.actionBarBackButton:
                if(current_postion==0){
                    finish();
                }else{
                    current_postion--;
                    myViewPager.setCurrentItem(current_postion,true);
                }
                break;
            default:
                break;

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_REGISTER)){
            current_postion= Integer.parseInt(messageEvent.getMessage());
            if(current_postion==-1){
                finish();
            }else{
                myViewPager.setCurrentItem(current_postion,true);
            }
        }


    }


    @Override
    public boolean isSwipeEnable() {
        return false;
    }


    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(current_postion==0){
                finish();
            }else{
                current_postion--;
                myViewPager.setCurrentItem(current_postion,true);
            }
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EasyHttp.cancel(this);
    }
}
