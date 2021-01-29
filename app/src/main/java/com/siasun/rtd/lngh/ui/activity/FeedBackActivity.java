package com.siasun.rtd.lngh.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MD5Utils;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.AdviceApi;
import com.siasun.rtd.lngh.http.request.QueryUserIdApi;
import com.siasun.rtd.lngh.http.response.QueryUserIdResponse;
import com.siasun.rtd.lngh.http.response.SubmitFeedBackResponseDTO;
import com.siasun.rtd.lngh.ui.dialog.MessageDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;

public class FeedBackActivity extends MyActivity {
    private TextView tv_edit_count;

    private EditText et_feedback_info,et_feedback_connects;

    private Button btn_feedback_submit;
    @Override
    protected int getLayoutId() {
        return R.layout.feed_back_activity;
    }

    @Override
    protected void initView() {
        tv_edit_count= (TextView) findViewById(R.id.tv_edit_count);
        et_feedback_info= (EditText) findViewById(R.id.et_feedback_info);
        et_feedback_connects= (EditText) findViewById(R.id.et_feedback_connects);
        btn_feedback_submit= (Button) findViewById(R.id.btn_feedback_submit);

        setOnClickListener(R.id.btn_feedback_submit);
    }

    @Override
    protected void initData() {
        et_feedback_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputStr =  et_feedback_info.getText().toString().trim();
                tv_edit_count.setText(inputStr.length() + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_feedback_submit:
                if(TextUtils.isEmpty(et_feedback_info.getText().toString())){
                    toast("意见不得为空");
                    return ;
                }
                showDialog();

                EasyHttp.post(this)
                        .api(new AdviceApi().setToken(Const.Tk).setContent(et_feedback_info.getText().toString()))
                        .request(new HttpCallback<SubmitFeedBackResponseDTO>(this){
                            @Override
                            public void onSucceed(SubmitFeedBackResponseDTO responseDTO) {
                                hideDialog();
                                if(responseDTO.status.equals("0")&&responseDTO.msg.equals("success")) {
                                    toast("提交成功");
                                }else{
                                    toast("提交失败请重试");
                                }
                                finish();
                            }

                            @Override
                            public void onEnd(Call call) {
                                super.onEnd(call);
                                hideDialog();
                            }
                        });


                break;
            default:
                break;
        }
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }
}
