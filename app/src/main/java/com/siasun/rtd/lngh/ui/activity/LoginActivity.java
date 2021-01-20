package com.siasun.rtd.lngh.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.health.SystemHealthManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.siasun.rtd.lngh.BuildConfig;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.DebugLog;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.helper.InputTextHelper;
import com.siasun.rtd.lngh.http.bean.LoginRequestBean;
import com.siasun.rtd.lngh.http.bean.ResponseMsgBean;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.LoginApi;
import com.siasun.rtd.lngh.http.response.LoginResponseBean;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.other.KeyboardWatcher;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {

    @DebugLog
    public static void start(Context context, String phone, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentKey.PHONE, phone);
        intent.putExtra(IntentKey.PASSWORD, password);
        context.startActivity(intent);
    }
    private View mLogoView;

    private ViewGroup mBodyLayout;
    private EditText mPhoneView;
    private EditText mPasswordView;

    private View mForgetView;
    private Button mCommitView;

    private View mBlankView;

    /** logo 缩放比例 */
    private final float mLogoScale = 0.8f;
    /** 动画时间 */
    private final int mAnimTime = 300;

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }


    @Override
    protected void initView() {
        mLogoView = findViewById(R.id.iv_login_logo);
        mBodyLayout = findViewById(R.id.ll_login_body);
        mPhoneView = findViewById(R.id.et_login_phone);
        mPasswordView = findViewById(R.id.et_login_password);
        mForgetView = findViewById(R.id.tv_login_forget);
        mCommitView = findViewById(R.id.btn_login_commit);
        mBlankView = findViewById(R.id.v_login_blank);


        setOnClickListener(mForgetView, mCommitView);

        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .build();
    }


    @Override
    protected void initData() {

        postDelayed(() -> {
            // 因为在小屏幕手机上面，因为计算规则的因素会导致动画效果特别夸张，所以不在小屏幕手机上面展示这个动画效果
            if (mBlankView.getHeight() > mBodyLayout.getHeight()) {
                // 只有空白区域的高度大于登录框区域的高度才展示动画
                KeyboardWatcher.with(LoginActivity.this)
                        .setListener(LoginActivity.this);
            }
        }, 500);



        // 填充传入的手机号和密码
        //mPhoneView.setText(getString(IntentKey.PHONE));
        //mPasswordView.setText(getString(IntentKey.PASSWORD));
        mPhoneView.setText("18240349259");
        mPasswordView.setText("p12345678");
    }



    @Override
    public void onRightClick(View v) {
        /*// 跳转到注册界面
        startActivityForResult(RegisterActivity.class, (resultCode, data) -> {
            // 如果已经注册成功，就执行登录操作
            if (resultCode == RESULT_OK && data != null) {
                mPhoneView.setText(data.getStringExtra(IntentKey.PHONE));
                mPasswordView.setText(data.getStringExtra(IntentKey.PASSWORD));
                mPasswordView.setSelection(mPasswordView.getText().length());
                onClick(mCommitView);
            }
        });*/
    }



    @SingleClick
    @Override
    public void onClick(View v) {
        if (v == mForgetView) {
            //startActivity(PasswordForgetActivity.class);
        } else if (v == mCommitView) {
            if (mPhoneView.getText().toString().length() != 11) {
                toast(R.string.common_phone_input_error);
                return;
            }
            LoginRequestBean bean = new LoginRequestBean();
            bean.platform = Const.PLATFORM;
            bean.dev_id = Const.DeviceId;
            bean.phone_number = mPhoneView.getText().toString();
            bean.pw = MD5Utils.MD5(mPasswordView.getText().toString());
            bean.dev_info = Const.DevInfo;
            bean.app_version = BuildConfig.VERSION_NAME;
            String requestString = new Gson().toJson(bean);

            String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));
            Log.e("test",requestString);
            Log.e("test",requestMsg);


            EasyHttp.post(this)
                    .api(new LoginApi()
                            .setRequestBody(requestMsg))
                    .request(new DecryptCallBack<String>(this) {

                        @Override
                        public void onSucceed(String result) {
                            Log.e("test3",result);

                            toast(result);
                            LoginResponseBean responseBean=new Gson().fromJson(result,LoginResponseBean.class);

                            if ("0".equals(responseBean.result)){
                                Const.Tk = responseBean.token;
                                SharedPreferenceUtil.getInstance().put(LoginActivity.this,IntentKey.PHONE,mPhoneView.getText().toString());
                                SharedPreferenceUtil.getInstance().put(LoginActivity.this,IntentKey.TOKEN,responseBean.token);
                                if(!TextUtils.isEmpty(responseBean.alias)){
                                    SharedPreferenceUtil.getInstance().put(LoginActivity.this,"alias",responseBean.alias);
                                }
                                if(!TextUtils.isEmpty(responseBean.identification)){
                                    SharedPreferenceUtil.getInstance().put(LoginActivity.this,"identification",responseBean.identification);
                                }

                                showDialog();
                                postDelayed(() -> {
                                  hideDialog();
                                  startActivity(MainTabActivity.class);
                                  finish();
                                }, 2000);


                            }else {
                                toast(responseBean.msg);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            toast(e.toString());
                        }
                    });



        }
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        // 获取这个 View 在屏幕中的坐标（左上角）
        mBodyLayout.getLocationOnScreen(location);
        //int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + mBodyLayout.getHeight());
        if (keyboardHeight > bottom){
            // 执行位移动画
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", 0, -(keyboardHeight - bottom));
            objectAnimator.setDuration(mAnimTime);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();

            // 执行缩小动画
            mLogoView.setPivotX(mLogoView.getWidth() / 2f);
            mLogoView.setPivotY(mLogoView.getHeight());
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", 1.0f, mLogoScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", 1.0f, mLogoScale);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", 0.0f, -(keyboardHeight - bottom));
            animatorSet.play(translationY).with(scaleX).with(scaleY);
            animatorSet.setDuration(mAnimTime);
            animatorSet.start();
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        if (mLogoView.getTranslationY() == 0){
            return;
        }
        // 执行放大动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", mLogoScale, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", mLogoScale, 1.0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", mLogoView.getTranslationY(), 0);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}
