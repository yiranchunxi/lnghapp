package com.siasun.rtd.lngh.http.prefs;

public class Const {

    public static String DeviceId;
    public static String Tk;
    public static String DevInfo;
    public static String PLATFORM="0";
    public static String LNGH = "lngh";
    public static String AUTH_PSY="auth_psy";
    public static String AUTH_LEGAL="auth_legal";

    public static final int ERROR_DECODE_MSG_ERROR = 0xE0000;
    public static final int ERROR_TOKEN_TIME_UP = 0xE1001;
    public static final int ERROR_TOKEN_WRONG = 0xE1002;
    public static final int ERROR_OTHER_WRONG = 0xE1003;
    public static final int ERROR_CAN_NOT_DEC_DATA = 0xE1005;
    public static final String URL_LNGH_SERVER="http://app.lnszgh.org";
    public static final String PRIVACY_LISENCE_PAGE_URL="http://app.lnszgh.org/lgh/views/privacy/lisence.html";
    public static final String PRIVACY_PROTECT_PAGE_URL="http://app.lnszgh.org/lgh/views/privacy/protect.html";
    public static final String COLLECT_PAGE_URL = "http://app.lnszgh.org/lgh/views/circle/favourite.html";
    public static final int JPUSH_SET_ALIAS=0xE1006;
    public static final int JPUSH_SET_TAGS=0xE1007;
    public static final int JPUSH_DELETE_ALIAS=0xE1008;
    public static final int JPUSH_DELETE_TAGS=0xE1009;
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


    //关闭所有网页
    public static final String EVENT_TAG_SHOW_LOGIN_SCENE="event_tag_show_login_scene";

    //显示心理咨询
    public static final String EVENT_TAG_SHOW_PSYCOUNSELING_SCENE="event_tag_show_psycounseling_scene";

    //显示法律咨询
    public static final String EVENT_TAG_SHOW_LEGAL_AID_SCENE="event_tag_show_legal_aid_scene";

    //显示职工书屋
    public static final String EVENT_TAG_SHOW_STAFF_BOOKSTORE_SCENE="event_tag_show_staff_bookstore_scene";

    //注册切换页面
    public static final String EVENT_TAG_REGISTER="event_tag_register";
}
