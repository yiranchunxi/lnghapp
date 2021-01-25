package com.siasun.rtd.lngh.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.QueryUserInfoRequestDTO;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.glide.GlideApp;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.LoginApi;
import com.siasun.rtd.lngh.http.request.QueryUserInfoApi;
import com.siasun.rtd.lngh.http.response.LoginResponseBean;
import com.siasun.rtd.lngh.http.response.QueryUserInfoResponseDTO;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.LoginActivity;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;

/**
 * 我的fragment
 */
public final class MeFragment extends MyFragment<MainTabActivity> {

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.me_fragment;
    }

    private View mGotoLoginButton,mGotoVerifyButton;
    private View mUserNameContainer;
    private TextView mUserNameTextView;
    private ImageView avatar,iv_tooltip;
    private ImageView iv_me_realname_medal,iv_me_union_medal;
    private AnimatorSet set;
    @Override
    protected void initView() {
        mGotoLoginButton=findViewById(R.id.gotoLoginButton);
        mGotoVerifyButton=findViewById(R.id.gotoVerifyButton);
        mUserNameContainer = findViewById(R.id.userNameContainer);
        mUserNameTextView = findViewById(R.id.userNameTextView);
        iv_me_realname_medal=findViewById(R.id.iv_me_realname_medal);
        iv_me_union_medal=findViewById(R.id.iv_me_union_medal);
        avatar=findViewById(R.id.avatar);
        iv_tooltip=findViewById(R.id.iv_tooltip);
        setOnClickListener(mGotoLoginButton);


        set=new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(iv_tooltip,"scaleX",0.5f,1.1f,1f),
                ObjectAnimator.ofFloat(iv_tooltip,"scaleY",0.5f,1.1f,1f),
                ObjectAnimator.ofFloat(iv_tooltip,"alpha",0.5f,1f)
        );
    }

    @Override
    protected void initData() {

    }
    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gotoLoginButton:
                startActivity(LoginActivity.class);

            default:
                break;
        }
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


    @Override
    public void onResume() {
        super.onResume();
        toast("onResume");
        refreshUserInfo();

    }
    private void refreshUserInfo(){
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(getAttachActivity(),IntentKey.TOKEN))){
            mGotoLoginButton.setVisibility(View.INVISIBLE);
            Const.Tk=SharedPreferenceUtil.getInstance().get(getAttachActivity(),IntentKey.TOKEN);
            queryUserInfo();
        }else{
            //未登录
            GlideApp.with(getAttachActivity())
                    .load(R.drawable.icon_me_photo)
                    .into(avatar);
            avatar.setOnClickListener(null);
            mGotoVerifyButton.setVisibility(View.INVISIBLE);
            mUserNameContainer.setVisibility(View.INVISIBLE);
            iv_me_realname_medal.setVisibility(View.INVISIBLE);
            iv_me_union_medal.setVisibility(View.INVISIBLE);
            iv_tooltip.setVisibility(View.VISIBLE);
            set.setDuration(500).start();
        }
    }

    private void queryUserInfo(){

        QueryUserInfoRequestDTO requestDTO = new QueryUserInfoRequestDTO();
        requestDTO.platform = Const.PLATFORM;
        requestDTO.token = Const.Tk;
        String requestString = new Gson().toJson(requestDTO);
        //LogUtils.v(requestString);
        Log.e("test",requestString);
        String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, true, MsgHandler.EncType.ENC_TYPE_2));

        EasyHttp.post(this)
                .api(new QueryUserInfoApi()
                        .setRequestBody(requestMsg))

                .request(new DecryptCallBack<String>(this, new DecryptCallBack.ChildrenCallBack() {
                    @Override
                    public void onSucceed(String result) {
                        toast(result);
                        QueryUserInfoResponseDTO bean = new Gson().fromJson(result, QueryUserInfoResponseDTO.class);
                        if ("0".equals(bean.result)) {
                            if ("false".equals(bean.certificated)) {
                                //未认证
                                SharedPreferenceUtil.getInstance().put(getAttachActivity(),"certificated",bean.certificated);

                                GlideApp.with(getAttachActivity())
                                        .load(R.drawable.icon_me_photo)
                                        .into(avatar);
                                avatar.setOnClickListener(getAttachActivity());
                                iv_me_realname_medal.setVisibility(View.INVISIBLE);
                                iv_me_union_medal.setVisibility(View.INVISIBLE);
                                mUserNameContainer.setVisibility(View.INVISIBLE);
                                mGotoVerifyButton.setVisibility(View.VISIBLE);

                                iv_tooltip.setVisibility(View.VISIBLE);
                                set.setDuration(500).start();


                            } else {
                                //已认证
                                SharedPreferenceUtil.getInstance().put(getAttachActivity(),"certificated",bean.certificated);
                                SharedPreferenceUtil.getInstance().put(getAttachActivity(),"user_name",bean.user_name);
                                if(!TextUtils.isEmpty(bean.org_name))
                                    SharedPreferenceUtil.getInstance().put(getAttachActivity(),"org_name",bean.org_name);
                                SharedPreferenceUtil.getInstance().put(getAttachActivity(),"avatar",bean.avatar);
                                if(!TextUtils.isEmpty(bean.tag)){
                                    SharedPreferenceUtil.getInstance().put(getAttachActivity(),"tag",bean.tag);
                                }
                                mUserNameTextView.setText(bean.user_name);
                                avatar.setOnClickListener(getAttachActivity());
                                mUserNameContainer.setVisibility(View.VISIBLE);
                                iv_me_realname_medal.setVisibility(View.VISIBLE);
                                iv_me_union_medal.setVisibility(View.VISIBLE);
                                mGotoVerifyButton.setVisibility(View.INVISIBLE);
                                iv_tooltip.setVisibility(View.INVISIBLE);



                            }
                        } else {
                            toast(bean.msg);
                        }
                    }
                }) {
                    @Override
                    public void onFail(Exception e) {
                        toast(e.toString());
                    }
                });


    }
}
