package com.siasun.rtd.lngh.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.widget.layout.WrapRecyclerView;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.aop.SingleClick;
import com.siasun.rtd.lngh.common.MyActivity;
import com.siasun.rtd.lngh.http.bean.MessageEvent;
import com.siasun.rtd.lngh.http.bean.SendCircleRequestDTO;
import com.siasun.rtd.lngh.http.callback.DecryptCallBack;
import com.siasun.rtd.lngh.http.prefs.Const;
import com.siasun.rtd.lngh.http.prefs.MsgHandler;
import com.siasun.rtd.lngh.http.prefs.Utils;
import com.siasun.rtd.lngh.http.request.SendCircle;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SendCircleActivity extends MyActivity{

    private EditText sendCircleEditText;
    private WrapRecyclerView mRecyclerView;
    private ImageView mVideoPreviewImgView;
    private LinearLayout mPreviewLayout;
    private ImageView mPreviewImageView;
    private TextView mPreviewPageTitleTextView;

    //随机消息标识
    private String mMsgRefSn = "";
    //选择的图片
    private ArrayList<String> mSelImageList;
    //是否是视频
    private boolean mIsVideo = false;

    private String mOutputVideoPath;
    private String mThumbnailFilePath;
    private String mLinkTitle;
    private String mLinkImgLocalPath;
    private String mLinkUrl;
    //消息类型
    private int mMsgType;
    @Override
    protected int getLayoutId() {
        return R.layout.send_circle_activity;
    }

    @Override
    protected void initView() {
        sendCircleEditText=findViewById(R.id.sendCircleEditText);

        setOnClickListener(R.id.addImgButton,R.id.addStickerButton,R.id.addLinkButton);
    }

    @Override
    protected void initData() {
        mMsgRefSn=generateMsgRefSn();
        mSelImageList=new ArrayList<>();
    }

    @SingleClick
    @Override
    public void onRightClick(View v) {
        judgeMsgType();
        if (mMsgType == Const.CIRCLE_TYPE_TEXT && sendCircleEditText.getText().toString().trim().length() == 0){
            toast(R.string.circleMsgCannotBeEmpty);
            return;
        }

        startSendCircleMsg();
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addImgButton:
                ImageSelectActivity.start(this, new ImageSelectActivity.OnPhotoSelectListener() {

                    @Override
                    public void onSelected(List<String> data) {
                        toast("选择了" + data.toString());
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            default:
                break;
        }
    }

    private void startSendCircleMsg(){
        showDialog();
        switch (mMsgType){
            case Const.CIRCLE_TYPE_TEXT:
                sendCircleMsg("");
                break;

            default:
                break;

        }


    }

    private void sendCircleMsg(String content){
        SendCircleRequestDTO requestDTO = new SendCircleRequestDTO();
        requestDTO.token = Const.Tk;
        requestDTO.m_ref_sn = mMsgRefSn;
        // TODO: 2017/12/15 location
        requestDTO.location_flag = "1";

        requestDTO.msg_text = sendCircleEditText.getText().toString().trim();
        requestDTO.type = mMsgType + "";
        requestDTO.content = content;

        String requestString = new Gson().toJson(requestDTO);
        //LogUtils.v(requestString);
        Log.e("test",requestString);
        String requestMsg = MsgHandler.msgEncode(MsgHandler.createRequestMsg(requestString, true, MsgHandler.EncType.ENC_TYPE_2));

        EasyHttp.post(this)
                .api(new SendCircle().setRequestBody(requestMsg))
                .request(new DecryptCallBack(this, new DecryptCallBack.ChildrenCallBack() {
                    @Override
                    public void onSucceed(String result) {
                        hideDialog();
                        LogoutResponseBean responseBean=new Gson().fromJson(result,LogoutResponseBean.class);
                        if("0".equals(responseBean.result)){
                            //通知工会圈 服务 惠商城页面刷新
                            EventBus.getDefault().post(new MessageEvent(Const.EVENT_TAG_SUCCESS_LOGIN,"event_tag_success_login"));
                            finish();
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

    private String generateMsgRefSn(){
        return Utils.getStringDate() + String.format("%05d", Utils.createRandom(0, 99999));
    }

    private void judgeMsgType(){
        int result = Const.CIRCLE_TYPE_TEXT;
        if (mSelImageList.size() != 0){
            result = Const.CIRCLE_TYPE_IMG;
        }
        if (mIsVideo){
            result = Const.CIRCLE_TYPE_VIDEO;
        }
        // TODO: 2017/12/15 video、link type
        if (!TextUtils.isEmpty(mLinkUrl)){
            result = Const.CIRCLE_TYPE_LINK;
        }
        mMsgType = result;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyHttp.cancel(this);
    }
}
