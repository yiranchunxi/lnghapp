package com.siasun.rtd.lngh.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.ApplyRegisterRequestBean;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.ApplyRegister;
import com.siasun.rtd.lngh.http.request.VerifyRegister;
import com.siasun.rtd.lngh.http.request.VerifyRegisterRequestBean;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;
import com.siasun.rtd.lngh.widget.VerifyCodeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 注册fragment
 */
public final class RegisterVerifyFragment extends MyFragment<MainTabActivity> {

    private VerifyCodeView verifyCodeView;
    private TextView tv_register_verify_tag,tv_register_verify_time;
    private String firstPhone="",phone_num;
    private  int mSendMsgTimeLeft;

    private  final int TIMER_COUNT_DOWN = 0x01;
    private Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER_COUNT_DOWN:
                    mSendMsgTimeLeft--;
                    if (mSendMsgTimeLeft <= 0){
                        tv_register_verify_time.setEnabled(true);
                        tv_register_verify_time.setText(getString(R.string.register_new_tag6));
                    }else {
                        tv_register_verify_time.setText(mSendMsgTimeLeft + getString(R.string.register_new_tag2));
                        mTimerHandler.sendEmptyMessageDelayed(TIMER_COUNT_DOWN, 1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    public static RegisterVerifyFragment newInstance() {
        return new RegisterVerifyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_verify;
    }

    @Override
    protected void initView() {
        verifyCodeView=findViewById(R.id.verify_code_view);

        tv_register_verify_tag=findViewById(R.id.tv_register_verify_tag);

        tv_register_verify_time=findViewById(R.id.tv_register_verify_time);


        setOnClickListener(R.id.tv_register_verify_time);




        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {

                showDialog();
                VerifyRegisterRequestBean bean=new VerifyRegisterRequestBean();

                bean.phone_number=phone_num;
                bean.v_code=verifyCodeView.getEditContent();

                String requestString = new Gson().toJson(bean);
                String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));
                EasyHttp.post(RegisterVerifyFragment.this)
                        .api(new VerifyRegister().setRequestBody(requestMsg))
                        .request(new DecryptCallBack(RegisterVerifyFragment.this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {
                                hideDialog();

                                LogoutResponseBean responseBean=new Gson().fromJson(result,LogoutResponseBean.class);
                                if("0".equals(responseBean.result)){
                                    EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_REGISTER,"2"));
                                }else{
                                    toast(responseBean.msg);
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
            public void invalidContent() {

            }
        });
    }

    @Override
    protected void initData() {
        phone_num= SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.PHONE);

        if(!TextUtils.isEmpty(phone_num)){
            tv_register_verify_tag.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_register_verify_tag.setText(getString(R.string.register_new_tag1).replace("X",phone_num));
                }
            },1000);


            if(!firstPhone.equals(phone_num)){

                tv_register_verify_time.setEnabled(false);
                mSendMsgTimeLeft = 60;
                tv_register_verify_time.setText(mSendMsgTimeLeft + getString(R.string.register_new_tag2));
                mTimerHandler.sendEmptyMessageDelayed(TIMER_COUNT_DOWN, 1000);


            }else{



            }

            firstPhone=phone_num;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id. tv_register_verify_time:
                showDialog();
                ApplyRegisterRequestBean bean=new ApplyRegisterRequestBean();
                bean.phone_number=phone_num;
                String requestString = new Gson().toJson(bean);

                String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));


                EasyHttp.post(RegisterVerifyFragment.this)
                        .api(new ApplyRegister().setRequestBody(requestMsg))
                        .request(new DecryptCallBack(RegisterVerifyFragment.this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {
                                hideDialog();
                                LogoutResponseBean responseBean=new Gson().fromJson(result,LogoutResponseBean.class);
                                if("0".equals(responseBean.result)){
                                    tv_register_verify_time.setEnabled(false);
                                    mSendMsgTimeLeft = 60;
                                    tv_register_verify_time.setText(mSendMsgTimeLeft + getString(R.string.register_new_tag2));
                                    mTimerHandler.sendEmptyMessageDelayed(TIMER_COUNT_DOWN, 1000);
                                }else{
                                    toast(responseBean.msg);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                hideDialog();
                                toast(e.getMessage());
                            }
                        }

                        ));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimerHandler.removeCallbacksAndMessages(null);

        EasyHttp.cancel(this);
    }
}
