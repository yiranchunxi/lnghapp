package com.siasun.rtd.lngh.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.ApplyRegisterRequestBean;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.prefs.Utils;
import com.siasun.rtd.lngh.http.request.ApplyRegister;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 注册fragment
 */
public final class RegisterPhoneFragment extends MyFragment<MainTabActivity> {
    private TextView tv_next;
    private EditText et_register_phone;
    private ImageView iv_phone_clear;
    private long startTime;
    private int per=1000;
    private int second=60;
    public static RegisterPhoneFragment newInstance() {
        return new RegisterPhoneFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_phone;
    }

    @Override
    protected void initView() {

        tv_next=findViewById(R.id.tv_next);
        et_register_phone=findViewById(R.id.et_register_phone);
        iv_phone_clear=findViewById(R.id.iv_phone_clear);


    }

    @Override
    protected void initData() {


        iv_phone_clear.setOnClickListener(v -> {
            et_register_phone.setText("");
        });
        et_register_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.isEmpty(s.toString())){

                    tv_next.setTextColor(getResources().getColor(R.color.colorb2b2b2));
                    tv_next.setOnClickListener(null);
                }else{
                    tv_next.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
                    tv_next.setOnClickListener(v -> {
                        if (!Utils.checkPhone(s.toString())){
                            toast("请输入正确的手机号");
                            return;
                        }

                        String phone_num= SharedPreferenceUtil.getInstance().get(getAttachActivity(),IntentKey.PHONE);
                        if(phone_num.equals(s.toString())&&(System.currentTimeMillis()-startTime)/per<second){
                            EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_REGISTER,"1"));
                        }else{
                            showDialog();
                            SharedPreferenceUtil.getInstance().put(getAttachActivity(),IntentKey.PHONE,s.toString().trim());

                            ApplyRegisterRequestBean bean=new ApplyRegisterRequestBean();
                            bean.phone_number=s.toString().trim();
                            String requestString = new Gson().toJson(bean);

                            String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));


                            EasyHttp.post(RegisterPhoneFragment.this)
                                    .api(new ApplyRegister().setRequestBody(requestMsg))
                                    .request(new DecryptCallBack(RegisterPhoneFragment.this, new DecryptCallBack.ChildrenCallBack() {
                                        @Override
                                        public void onSucceed(String result) {
                                            hideDialog();
                                            LogoutResponseBean responseBean=new Gson().fromJson(result,LogoutResponseBean.class);
                                            if("0".equals(responseBean.result)){
                                                startTime=System.currentTimeMillis();
                                                EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_REGISTER,"1"));
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
                        }


                    });

                }


            }
        });

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return false;
    }
    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyHttp.cancel(this);
    }
}
