package com.siasun.rtd.lngh.ui.activity;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
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
import com.siasun.rtd.lngh.http.prefs.SharedPreferenceUtil;
import com.siasun.rtd.lngh.http.prefs.Utils;
import com.siasun.rtd.lngh.http.request.SendCircle;
import com.siasun.rtd.lngh.http.response.LogoutResponseBean;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SendCircleActivity extends MyActivity{

    private EditText sendCircleEditText;

    private RelativeLayout mAddImgButton;
    private RelativeLayout mAddStickerButton;
    private RelativeLayout mAddLinkButton;

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
    //判断是粘贴板分享还是新闻分享
    private String Login_Flag;
    //新闻分享url
    private String share_url;
    //新闻分享title
    private String title;


    //解析url Task
    private class parseLinkTask extends AsyncTask<Void,Map,Map>{
        private String url_index="?iid";
        private String linkUrl;

        public parseLinkTask(String url){
            super();
            this.linkUrl=url;
        }

        @Override
        protected Map doInBackground(Void... voids) {
            Map map = null;
            try {
                Document document;
                //判断是否是从今日头条过来的url
                if(linkUrl.contains(url_index)){

                    String[] t=linkUrl.split("/");
                    String temp=t[t.length-2];
                    if(temp.contains("a")||temp.contains("i")){

                    }else{
                        temp="i"+temp;
                    }
                    String base_url="https://www.toutiao.com/";

                    document = Jsoup.parse(new URL(base_url+temp+t[t.length-1]), 10000);
                }else{


                    BufferedReader br = null;

                    String content="";
                    try{
                        URLConnection connection =  new URL(linkUrl).openConnection();
                        connection.setConnectTimeout(10000);

                        Scanner scanner = new Scanner(connection.getInputStream());


                        while (scanner.hasNextLine()) {


                            String line = scanner.nextLine();


                            content = content +line;
                        }

                    } catch (MalformedURLException e) {


                        e.printStackTrace();


                    } catch (IOException e) {


                        e.printStackTrace();



                    }


                    document = Jsoup.parse(content);
                }


                //这里开始是做一个解析，需要在非UI线程进行
                String  imgStr="";


                String title = document.head().getElementsByTag("title").text();
                Elements imgs = document.getElementsByTag("img");//取得所有Img标签的值
                //Elements imports = document.select("link[rel=shortcut icon]");


                Elements tt=document.select("img[src~=(?i)\\.(png|jpe?g)]");


                for(int i=0;i<tt.size();i++){
                    //Log.e("shape_glide_placeholder",tt.get(i).id());
                    //Log.e("shape_glide_placeholder",tt.get(i).tagName());

                    List<Attribute> list=tt.get(i).attributes().asList();

                    for(int j=0;j<list.size();j++){

                        imgStr=list.get(j).getValue();
                        if(!TextUtils.isEmpty(imgStr)&&(imgStr.endsWith("png")||imgStr.endsWith("jpg"))&&imgStr.contains("http")){
                            break;
                        }else{
                            imgStr="";
                        }
                    }
                    if(!TextUtils.isEmpty(imgStr)&&(imgStr.endsWith("png")||imgStr.endsWith("jpg"))&&imgStr.contains("http")){
                        break;
                    }else{
                        imgStr="";
                    }
                }

                if(imgs.size()>0&&TextUtils.isEmpty(imgStr)){

                    for(int i=0;i<imgs.size();i++){
                        imgStr = imgs.get(i).attr("abs:src");//默认取第一个为图片
                        if(!TextUtils.isEmpty(imgStr)){
                            break;
                        }
                    }
                }
                if (linkUrl.indexOf("weixin.qq.com") != -1){
                    imgStr = imgs.get(1).attr("data-src");
                    if(TextUtils.isEmpty(title)){
                        title=document.getElementById("profileBt").getElementsByTag("a").text();
                    }
                }

                if (TextUtils.isEmpty(imgStr)){
                    imgStr = "";
                    mLinkImgLocalPath = "";
                }else {

                    FutureTarget<File> futureTarget = Glide.with(SendCircleActivity.this).load(imgStr).downloadOnly(192, 192);
                    try {
                        File file = futureTarget.get();
                        mLinkImgLocalPath = file.getAbsolutePath();

                    } catch (InterruptedException e) {
                        Log.e("parse",e.toString());
                    } catch (ExecutionException e) {
                        Log.e("parse",e.toString());
                    }
                }
                map=new HashMap() ;

                map.put("title",title);
                map.put("url",linkUrl);
                map.put("img",imgStr);

            } catch (Exception e) {
                Log.e("parse",e.toString());
            }

            return map;
        }

        @Override
        protected void onPostExecute(Map map){
            hideDialog();
            if(map==null||TextUtils.isEmpty(map.get("title").toString())){
                //loadWithWeb(linkUrl,map);
                refreshLinkPreview(linkUrl, "", linkUrl);
            }else{
                setClickTypeLink();
                refreshLinkPreview(map.get("title").toString(), map==null?"":map.get("img").toString(), linkUrl);
            }
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.send_circle_activity;
    }

    @Override
    protected void initView() {
        sendCircleEditText=findViewById(R.id.sendCircleEditText);
        mAddImgButton = findViewById(R.id.addImgButton);
        mAddStickerButton =findViewById(R.id.addStickerButton);
        mAddLinkButton = findViewById(R.id.addLinkButton);

        setOnClickListener(R.id.addImgButton,R.id.addStickerButton,R.id.addLinkButton);
        mPreviewLayout = findViewById(R.id.linkPreviewLayout);
        mPreviewImageView = findViewById(R.id.previewImageView);
        mPreviewPageTitleTextView = findViewById(R.id.pageTitleTextView);
    }

    @Override
    protected void initData() {
        mMsgRefSn=generateMsgRefSn();
        mSelImageList=new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            Login_Flag = bundle.getString("flag");
            share_url=bundle.getString("share_url");
            title=bundle.getString("title");
            if(Login_Flag.equals(Const.LOGIN_IN_CLIP_SUCCESS)){
                if(TextUtils.isEmpty(share_url)){
                    init_Clip();
                }else{
                    //setClickTypeLink();
                    //refreshLinkPreview(title, "", share_url);
                }

            }
        }
    }

    private void init_Clip(){
      if(TextUtils.isEmpty(SharedPreferenceUtil.getInstance().get(this,"clip_data"))){
          return;
      }
      showDialog();
      parseLink(SharedPreferenceUtil.getInstance().get(this,"clip_data"));
    }

    private void parseLink(String clip_data) {
        new parseLinkTask(clip_data).execute();
    }
    private void refreshLinkPreview(String title, String imgUrl, String pageUrl){



        mPreviewLayout.setVisibility(View.VISIBLE);
        mPreviewPageTitleTextView.setText(title);
        mLinkTitle = title;
        mLinkUrl = pageUrl;

        if (imgUrl == null || "".equals(imgUrl)){
            mLinkImgLocalPath = null;
        }else {
            Glide.with(this).load(imgUrl).into(mPreviewImageView);
        }
    }


    private void setClickTypeLink(){
        mAddImgButton.setEnabled(false);
        mAddStickerButton.setEnabled(false);
        mAddLinkButton.setEnabled(false);
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
            case Const.CIRCLE_TYPE_LINK:
                /*ImageUploaderBizSingleton.getInstance().setOnBizCallBackListener(this);
                if(mLinkImgLocalPath==null|| TextUtils.isEmpty(mLinkImgLocalPath)){
                    SendCircleLinkDTO sendCircleLinkDTO = new SendCircleLinkDTO();
                    sendCircleLinkDTO.web_img = "";
                    sendCircleLinkDTO.title = mLinkTitle;
                    sendCircleLinkDTO.webpage_url = mLinkUrl;
                    sendCircleMsg(JSON.toJSONString(sendCircleLinkDTO));
                }else{
                    ImageUploaderBizSingleton.getInstance().uploadLinkImg(mLinkImgLocalPath);
                }*/

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
                        toast(result);
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
