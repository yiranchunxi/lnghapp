package com.siasun.rtd.lngh.ui.fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.siasun.rtd.lngh.BuildConfig;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyFragment;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.bean.PasswordRequestBean;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.SubmitRegister;
import com.siasun.rtd.lngh.http.response.RegisterResponseBean;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.ui.activity.LoginActivity;
import com.siasun.rtd.lngh.ui.activity.MainTabActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

/**
 * 注册fragment
 */
public final class RegisterPasswordFragment extends MyFragment<MainTabActivity> {
    private ImageView iv_password_clear;
    private EditText et_register_password;
    private Button btn_register_password;
    private CheckBox cbDisplayPassword;
    public static RegisterPasswordFragment newInstance() {
        return new RegisterPasswordFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_password;
    }

    @Override
    protected void initView() {
        iv_password_clear=findViewById(R.id.iv_password_clear);
        et_register_password=findViewById(R.id.et_register_password);
        btn_register_password=findViewById(R.id.btn_register_password);
        cbDisplayPassword=findViewById(R.id.cbDisplayPassword);

        setOnClickListener(R.id.iv_password_clear,R.id.btn_register_password);
        cbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    et_register_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    et_register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_password_clear:
                et_register_password.setText("");
                break;
            case R.id.btn_register_password:
                String pattern = "^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\\\\W].*)(?=.*[0-9\\\\W].*).{8,}";
                if(!Pattern.matches(pattern, et_register_password.getText().toString())){
                    toast(R.string.passwordLengthMustAbove);
                    return;
                }

                showDialog();

                PasswordRequestBean bean = new PasswordRequestBean();

                bean.dev_id = Const.DeviceId;
                bean.phone_number = SharedPreferenceUtil.getInstance().get(getAttachActivity(), IntentKey.PHONE);
                bean.pw = MD5Utils.MD5(et_register_password.getText().toString());
                bean.dev_info = Const.DevInfo;
                bean.app_version = BuildConfig.VERSION_NAME;
                String requestString = new Gson().toJson(bean);
                String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, false, MsgHandler.EncType.ENC_TYPE_1));
                EasyHttp.post(RegisterPasswordFragment.this)
                        .api(new SubmitRegister().setRequestBody(requestMsg))
                        .request(new DecryptCallBack(RegisterPasswordFragment.this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {
                                hideDialog();
                                RegisterResponseBean pResponseBean = new Gson().fromJson(result, RegisterResponseBean.class);
                                if ("0".equals(pResponseBean.result)){
                                    toast("注册成功！");
                                    Const.Tk = pResponseBean.token;
                                    SharedPreferenceUtil.getInstance().put(getAttachActivity(),IntentKey.TOKEN,pResponseBean.token);
                                    if(!TextUtils.isEmpty(pResponseBean.alias)){
                                        SharedPreferenceUtil.getInstance().put(getAttachActivity(),"alias",pResponseBean.alias);
                                    }
                                    if(!TextUtils.isEmpty(pResponseBean.identification)){
                                        SharedPreferenceUtil.getInstance().put(getAttachActivity(),"identification",pResponseBean.identification);
                                    }
                                    EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_REGISTER,"-1"));
                                }else {
                                   toast(pResponseBean.msg);
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

    @Override
    protected void initData() {

    }
}
