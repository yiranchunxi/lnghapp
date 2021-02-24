package com.siasun.rtd.lngh.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.helper.ActivityStackManager;
import com.siasun.rtd.lngh.helper.DoubleClickHelper;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.bean.QueryShareUrlRequestBean;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.prefs.Utils;
import com.siasun.rtd.lngh.http.request.HasUpdate;
import com.siasun.rtd.lngh.http.request.QueryUserInfoApi;
import com.siasun.rtd.lngh.http.response.QueryShareUrlResponseBean;
import com.siasun.rtd.lngh.manager.AppThreadManger;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.other.KeyboardWatcher;
import com.siasun.rtd.lngh.ui.fragment.DiscoveryFragment;
import com.siasun.rtd.lngh.ui.fragment.HomeFragment;
import com.siasun.rtd.lngh.ui.fragment.MeFragment;
import com.siasun.rtd.lngh.ui.fragment.ServiceFragment;
import com.siasun.rtd.lngh.ui.fragment.UnionFragment;
import com.siasun.rtd.lngh.widget.RedPointView;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public final class MainTabActivity  extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener  {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    public  static boolean red_point=false;
    private RedPointView redPointView;
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

        initUnReadMessageViews();
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
        EventBus.getDefault().register(this);
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
                if(mViewPager.getCurrentItem()==1||red_point){
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
        if(mViewPager!=null)
        mViewPager.setAdapter(null);
        if(mBottomNavigationView!=null)
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
        EasyHttp.cancel(this);
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_SHOW_SERVICE_SCENE)){
            mBottomNavigationView.setSelectedItemId(R.id.menu_service);
        }
        if(messageEvent.getEventTag().equals(Const.EVENT_TAG_CIRCLE_POINT)){
            if(!TextUtils.isEmpty(messageEvent.getMessage())){
                if(!messageEvent.getMessage().equals("yes")){
                    SharedPreferenceUtil.getInstance().put(MainTabActivity.this,"circle_mid",messageEvent.getMessage());
                }
                red_point=false;
                redPointView.setVisibility(View.INVISIBLE);
            }else {
                red_point=true;
                redPointView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getClipBoardText(MainTabActivity.this, new Function() {
                    @Override
                    public void invoke(String text) {
                        if(!TextUtils.isEmpty(text)){
                            showClipDialog(text);
                        }
                    }
                });
               controllRedPoint();
            }
        },1000);
    }

    private void controllRedPoint() {
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(MainTabActivity.this, IntentKey.TOKEN))){

            String circle_mid = SharedPreferenceUtil.getInstance().get(MainTabActivity.this,"circle_mid");

            QueryShareUrlRequestBean bean=new QueryShareUrlRequestBean();

            bean.token=Const.Tk;

            if (TextUtils.isEmpty(circle_mid)) {
                bean.m_id="0";
            } else {
                bean.m_id=circle_mid;
            }

            String requestString = new Gson().toJson(bean);


            String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, true, MsgHandler.EncType.ENC_TYPE_2));

            EasyHttp.post(this)
                    .api(new HasUpdate()
                            .setRequestBody(requestMsg))
                    .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                        @Override
                        public void onSucceed(String result) {
                            toast(result);
                            QueryShareUrlResponseBean bean=new Gson().fromJson(result, QueryShareUrlResponseBean.class);

                            if ("0".equals(bean.result)){
                                red_point=true;
                                redPointView.setVisibility(View.VISIBLE);

                            }else {
                                red_point=false;
                                redPointView.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    }));


        }
    }

    private void showClipDialog(String text) {
        String cache= SharedPreferenceUtil.getInstance().get(this,"clip_data");

        if(!text.equals(cache)&&!TextUtils.isEmpty(Utils.findUrl(text)))
        {
            new AlertDialog.Builder(this)
                    .setTitle("是否发表复制的链接")
                    .setMessage(Utils.findUrl(text))
                    .setPositiveButton("去发表", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //-1
                            if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(MainTabActivity.this, IntentKey.TOKEN))){
                                Intent intent=new Intent(MainTabActivity.this,SendCircleActivity.class);
                                intent.putExtra("flag",Const.LOGIN_IN_CLIP_SUCCESS);
                                startActivity(intent);
                            }else{
                              toast("请先登录");
                            }

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //-2
                        }
                    }).show();
        }
        SharedPreferenceUtil.getInstance().put(this,"clip_data",text);

    }

    public interface Function{
        void invoke(String text);
    }

    void getClipBoardText(@Nullable Activity activity, final Function f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && activity != null) {
            getTextFromClipFromAndroidQ(activity, f);
        } else {
            f.invoke(getTextFromClip());
        }
    }



    private String getTextFromClip() {
        ClipboardManager clipboardManager =
                (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == clipboardManager || !clipboardManager.hasPrimaryClip()) {
            return "";
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (null == clipData || clipData.getItemCount() < 1) {
            return "";
        }
        ClipData.Item item = clipData.getItemAt(0);
        if (item == null)
            return "";
        CharSequence clipText = item.getText();
        if (TextUtils.isEmpty(clipText))
            return "";
        else
            return clipText.toString();

    }

    /**
     * AndroidQ 获取剪贴板的内容
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private void getTextFromClipFromAndroidQ(@NonNull final Activity activity, final Function f) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ClipboardManager clipboardManager =
                        (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (null == clipboardManager || !clipboardManager.hasPrimaryClip()) {
                    f.invoke("");
                    return;
                }
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (null == clipData || clipData.getItemCount() < 1) {
                    f.invoke("");
                    return;
                }
                ClipData.Item item = clipData.getItemAt(0);
                if (item == null) {
                    f.invoke("");
                    return;
                }
                CharSequence clipText = item.getText();
                if (TextUtils.isEmpty(clipText))
                    f.invoke("");
                else
                    f.invoke(clipText.toString());
            }
        };
        activity.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @androidx.annotation.Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                activity.getWindow().getDecorView().removeCallbacks(runnable);
            }
        });
        activity.getWindow().getDecorView().post(runnable);
    }

    private  void initUnReadMessageViews(){
        //初始化红点view
        BottomNavigationMenuView menuView = null;
        for (int i = 0; i < mBottomNavigationView.getChildCount(); i++) {
            View child = mBottomNavigationView.getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                menuView = (BottomNavigationMenuView) child;
                break;
            }
        }

        if (menuView != null) {
            int dp8 = getResources().getDimensionPixelSize(R.dimen.space_8);
            BottomNavigationItemView.LayoutParams params = new BottomNavigationItemView.LayoutParams(dp8*3, dp8*3);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.leftMargin = dp8 * 3;
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);
            redPointView=new RedPointView(this);
            redPointView.setWidthAndHeight(getResources().getDimensionPixelSize(R.dimen.space_18),getResources().getDimensionPixelSize(R.dimen.space_18));
            RelativeLayout.LayoutParams params1=new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.space_18),getResources().getDimensionPixelSize(R.dimen.space_18));
            redPointView.setLayoutParams(params1);
            redPointView.setVisibility(View.INVISIBLE);
            //redPointView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            itemView.addView(redPointView,params);
        }

    }
}
