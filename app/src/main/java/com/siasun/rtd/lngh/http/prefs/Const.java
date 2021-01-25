package com.siasun.rtd.lngh.http.prefs;

public class Const {

    public static String DeviceId;
    public static String Tk;
    public static String DevInfo;
    public static String PLATFORM="0";
    public static String LNGH = "lngh";


    public static final int ERROR_DECODE_MSG_ERROR = 0xE0000;
    public static final int ERROR_TOKEN_TIME_UP = 0xE1001;
    public static final int ERROR_TOKEN_WRONG = 0xE1002;
    public static final int ERROR_OTHER_WRONG = 0xE1003;
    public static final int ERROR_CAN_NOT_DEC_DATA = 0xE1005;


    //消息总线tag
    //显示入会界面 接收在servicefragment
    public static final String EVENT_TAG_SHOW_MEMBER_CERTIFICATION_SCENE="event_tag_show_member_certification_scene";

    //打开一个网页
    public static final String EVENT_TAG_SHOW_WEB_SCENE="event_tag_show_web_scene";

    //调用拨打电话
    public static final String EVENT_TAG_SHOW_CALL_SCENE="event_tag_show_call_scene";

    //关闭当前网页
    public static final String EVENT_TAG_EXIT_SCENE="event_tag_exit_scene";

    //关闭所有网页
    public static final String EVENT_TAG_EXIT_ROOT_SCENE="event_tag_exit_root_scene";
}
