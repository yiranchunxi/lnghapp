package com.siasun.rtd.lngh.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.bean.QueryAccountRequestBean;
import com.siasun.rtd.lngh.http.bean.ResetPwRequestBean;
import com.siasun.rtd.lngh.http.bean.SendSmsRequestBean;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.request.QueryAccount;
import com.siasun.rtd.lngh.http.request.ResetPw;
import com.siasun.rtd.lngh.http.request.SendSms;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;

import java.util.regex.Pattern;

public class ForgotPwActivity extends MyActivity {
    private EditText mPhoneNumberEditText;
    private EditText mNewPwEditText;
    private EditText mVcEditText;
    private Button mSendMsgBtn;
    private int mSendMsgTimeLeft;
    private String mPhoneNumberString;
    private final int TIMER_COUNT_DOWN = 0x01;

    private Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER_COUNT_DOWN:
                    mSendMsgTimeLeft--;
                    if (mSendMsgTimeLeft <= 0){
                        mSendMsgBtn.setEnabled(true);
                        mSendMsgBtn.setText(getString(R.string.clickToSendVerifyCode));
                    }else {
                        mSendMsgBtn.setText(mSendMsgTimeLeft + "");
                        mTimerHandler.sendEmptyMessageDelayed(TIMER_COUNT_DOWN, 1000);
                    }
                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_pw;
    }

    @Override
    protected void initView() {
        mSendMsgBtn =findViewById(R.id.sendVerifyCodeButton);

        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mNewPwEditText = findViewById(R.id.passwordEditText);
        mVcEditText = findViewById(R.id.verifyCodeEditText);

        setOnClickListener(R.id.resetNewPwButton,R.id.sendVerifyCodeButton);

    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.resetNewPwButton:
                if (TextUtils.isEmpty(mNewPwEditText.getText().toString())){
                    toast(R.string.pleaseInputNewPw);
                    return;
                }
                String pattern = "^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\\\\W].*)(?=.*[0-9\\\\W].*).{8,}";
                if(!Pattern.matches(pattern, mNewPwEditText.getText().toString())){
                    toast(R.string.passwordLengthMustAbove);
                    return;
                }
                if (TextUtils.isEmpty(mVcEditText.getText().toString())){
                    toast(R.string.pleaseInputVerifyCode);
                    return;
                }
                showDialog();

                ResetPwRequestBean resetBean = new ResetPwRequestBean();
                resetBean.platform = Const.PLATFORM;
                resetBean.phone_number = mPhoneNumberString;
                resetBean.pw = MD5Utils.MD5(mNewPwEditText.getText().toString());
                resetBean.v_code = mVcEditText.getText().toString();
                String resetString = new Gson().toJson(resetBean);
                String resetMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(resetString, false, MsgHandler.EncType.ENC_TYPE_1));

                EasyHttp.post(this)
                        .api(new ResetPw().setRequestBody(resetMsg))
                        .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {
                                hideDialog();
                                LogoutResponseBean bean = new Gson().fromJson(result,LogoutResponseBean.class);

                                if ("0".equals(bean.result)) {
                                    toast(R.string.changePwSuccessPleaseReLogin);
                                    postDelayed(() -> {
                                        finish();
                                    }, 1000);
                                } else {
                                    toast(bean.msg);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                hideDialog();
                                toast(e.getMessage());
                            }
                        }));

                break;
            case R.id.sendVerifyCodeButton:
                if (TextUtils.isEmpty(mPhoneNumberEditText.getText().toString())){
                    toast(R.string.pleaseInputYourPhoneNumber);
                    return;
                }
                showDialog();
                mPhoneNumberString = mPhoneNumberEditText.getText().toString();
                QueryAccountRequestBean bean = new QueryAccountRequestBean();
                bean.platform = Const.PLATFORM;
                bean.phone_number = mPhoneNumberString;
                String requestString = new Gson().toJson(bean);
                String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));

                EasyHttp.post(this)
                        .api(new QueryAccount().setRequestBody(requestMsg))
                        .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {

                                LogoutResponseBean bean = new Gson().fromJson(result,LogoutResponseBean.class);

                                if ("0".equals(bean.result)) {
                                    sendSms(mPhoneNumberString);
                                } else {
                                   toast(bean.msg);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                hideDialog();
                                toast(e.getMessage());
                            }
                        }));



                break;

            default:
                break;

        }
    }

    private  void sendSms(String phoneNumber){
            SendSmsRequestBean bean = new SendSmsRequestBean();
            bean.platform = Const.PLATFORM;
            bean.phone_number = phoneNumber;
            bean.biz_type = "1";
            String requestString = new Gson().toJson(bean);

            String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));
            EasyHttp.post(this)
                    .api(new SendSms().setRequestBody(requestMsg))
                    .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                        @Override
                        public void onSucceed(String result) {
                            hideDialog();
                            LogoutResponseBean bean = new Gson().fromJson(result,LogoutResponseBean.class);

                            if ("0".equals(bean.result)){
                               toast(R.string.sendSuccess);
                                mSendMsgBtn.setEnabled(false);
                                mSendMsgTimeLeft = 60;
                                mSendMsgBtn.setText(mSendMsgTimeLeft + "");
                                mTimerHandler.sendEmptyMessageDelayed(TIMER_COUNT_DOWN, 1000);
                            }else {
                               toast(bean.msg);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            hideDialog();
                            toast(e.getMessage());
                        }
                    }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerHandler.removeCallbacksAndMessages(null);
        EasyHttp.cancel(this);
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }
}
