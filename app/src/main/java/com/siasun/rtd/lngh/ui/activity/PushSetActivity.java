package com.siasun.rtd.lngh.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hjq.widget.view.SwitchButton;
import com.siasun.rtd.lngh.BuildConfig;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.other.IntentKey;
import com.siasun.rtd.lngh.receiver.PushConfig;

public class PushSetActivity extends MyActivity implements SwitchButton.OnCheckedChangeListener{

    private SwitchButton sb_push_info,sb_push_service,sb_push_union;
    private Context context;
    @Override
    protected int getLayoutId() {
        return R.layout.push_set_activity;
    }

    @Override
    protected void initView() {

        sb_push_info=findViewById(R.id.sb_push_info);
        sb_push_service=findViewById(R.id.sb_push_service);
        sb_push_union=findViewById(R.id.sb_push_union);


        sb_push_info.setOnCheckedChangeListener(this);
        sb_push_service.setOnCheckedChangeListener(this);
        sb_push_union.setOnCheckedChangeListener(this);

        setOnClickListener(R.id.sb_setting_info,R.id.sb_setting_service,R.id.sb_setting_union);
    }

    @Override
    protected void initData() {
        context=this;
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p1"))||SharedPreferenceUtil.getInstance().get(context,"p1").equals("p1"))
        {
            sb_push_info.setChecked(true);
        }else{
            sb_push_info.setChecked(false);
        }
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p2"))||SharedPreferenceUtil.getInstance().get(context,"p2").equals("p2")){
            sb_push_service.setChecked(true);
        }else{
            sb_push_service.setChecked(false);
        }
        if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context,"p3"))||SharedPreferenceUtil.getInstance().get(context,"p3").equals("p3")){
            sb_push_union.setChecked(true);
        }else{
            sb_push_union.setChecked(false);
        }
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sb_setting_info:
                sb_push_info.setChecked(!sb_push_info.isChecked());
                break;
            case R.id.sb_setting_service:
                sb_push_service.setChecked(!sb_push_service.isChecked());
                break;
            case R.id.sb_setting_union:
                sb_push_union.setChecked(!sb_push_union.isChecked());
                break;
            default:
                break;
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
        return false;
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
            switch (button.getId()){

                case R.id.sb_push_info:
                    if(isChecked){
                        SharedPreferenceUtil.getInstance().put(context,"p1","p1");
                    }else{
                        SharedPreferenceUtil.getInstance().put(context,"p1","no");
                    }
                    setPush();
                    break;
                case R.id.sb_push_service:
                    if(isChecked){
                        SharedPreferenceUtil.getInstance().put(context,"p2","p2");
                    }else{
                        SharedPreferenceUtil.getInstance().put(context,"p2","no");
                    }
                    setPush();
                    break;
                case R.id.sb_push_union:
                    if(isChecked){
                        SharedPreferenceUtil.getInstance().put(context,"p3","p3");
                    }else{
                        SharedPreferenceUtil.getInstance().put(context,"p3","no");
                    }
                    setPush();
                    break;
                default:
                    break;
            }
    }

    private void setPush(){
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(context, IntentKey.TOKEN))){
            PushConfig.startPush(context,SharedPreferenceUtil.getInstance().get(context, "alias"));
        }else{
            PushConfig.stopPush(context);
        }
    }
}
