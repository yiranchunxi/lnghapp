package com.siasun.rtd.lngh.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.widget.layout.SettingBar;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.helper.CacheDataManager;
import com.siasun.rtd.lngh.http.bean.QueryUserInfoRequestDTO;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.glide.GlideApp;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.request.LogOutApi;
import com.siasun.rtd.lngh.http.request.QueryUserInfoApi;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;
import com.siasun.rtd.lngh.http.response.QueryUserInfoResponseDTO;
import com.siasun.rtd.lngh.other.IntentKey;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

public class SetActivity  extends MyActivity {

    private SettingBar sb_setting_clear;
    private SettingBar sb_setting_notify;
    private SettingBar sb_setting_update;
    private SettingBar sb_setting_about;
    private SettingBar sb_setting_exit;
    @Override
    protected int getLayoutId() {
        return R.layout.set_activity;
    }

    @Override
    protected void initView() {
        sb_setting_clear=findViewById(R.id.sb_setting_clear);
        sb_setting_notify=findViewById(R.id.sb_setting_notify);
        sb_setting_update=findViewById(R.id.sb_setting_update);
        sb_setting_about=findViewById(R.id.sb_setting_about);
        sb_setting_exit=findViewById(R.id.sb_setting_exit);

        setOnClickListener(R.id.sb_setting_clear,R.id.sb_setting_notify,R.id.sb_setting_update,R.id.sb_setting_about,R.id.sb_setting_exit);
    }

    @Override
    protected void initData() {
        // 获取应用缓存大小
        sb_setting_clear.setRightText(CacheDataManager.getTotalCacheSize(this));
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sb_setting_exit:
                QueryUserInfoRequestDTO requestDTO = new QueryUserInfoRequestDTO();
                requestDTO.platform = Const.PLATFORM;
                requestDTO.token = Const.Tk;
                String requestString = new Gson().toJson(requestDTO);
                //LogUtils.v(requestString);
                Log.e("test",requestString);
                String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, true, MsgHandler.EncType.ENC_TYPE_2));

                EasyHttp.post(this)
                        .api(new LogOutApi()
                                .setRequestBody(requestMsg))

                        .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                            @Override
                            public void onSucceed(String result) {
                                toast(result);
                                LogoutResponseBean bean = new Gson().fromJson(result, LogoutResponseBean.class);
                                if ("0".equals(bean.result)) {
                                    Const.Tk="";
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, IntentKey.TOKEN);
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, IntentKey.PHONE);
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "alias");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "identification");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "certificated");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "certificated");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "user_name");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "org_name");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "avatar");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, "tag");
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, Const.AUTH_PSY);
                                    SharedPreferenceUtil.getInstance().clearConfig(SetActivity.this, Const.AUTH_LEGAL);
                                    toast("退出登录成功");
                                    finish();
                                } else {
                                    toast(bean.msg);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        }));
                break;
            case R.id.sb_setting_clear:

                // 清除内存缓存（必须在主线程）
                GlideApp.get(getActivity()).clearMemory();
                new Thread(() -> {
                    CacheDataManager.clearAllCache(this);
                    // 清除本地缓存（必须在子线程）
                    GlideApp.get(getActivity()).clearDiskCache();
                    post(() -> {
                        // 重新获取应用缓存大小
                        sb_setting_clear.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                    });
                }).start();
                break;
            case R.id.sb_setting_update:
                  Beta.checkUpgrade(false,false);
                  break;
            case R.id.sb_setting_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.sb_setting_notify:
                startActivity(PushSetActivity.class);
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
