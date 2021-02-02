package com.siasun.rtd.lngh.http.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/2.
 */

public class Utils {

    /**
     * 判断是否是URL
     * @param url
     * @return
     */
    public static boolean isUrl(String url){

        String URL_REGEX = "(((http|ftp|https)://)|(www\\.))[a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6}(:[0-9]{1,4})?(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        Pattern urlPattern = Pattern.compile(URL_REGEX);

        Matcher matcher = urlPattern.matcher(url);
        return  matcher.find();
    }

    /**
     * 简单的判别检测手机号是否合法
     * 后台做正则匹配
     */
    public static boolean checkPhone(String phone) {
        String pattern = "1[0-9]{10}";
        return phone.matches(pattern);
    }


    public static String findUrl(String url){


        //处理url匹配
        Pattern urlPattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher urlMatcher = urlPattern.matcher(url);
        while (urlMatcher.find()){
            return urlMatcher.group();
        }
        return "";
    }






    /**
     * 获取pdf intent
     * @param uri
     * @return
     */
    public static Intent getPdfFileIntent(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");

        return intent;
    }

    /**
     * 获取word intent
     * @param uri
     * @return
     */
    public static Intent getWordFileIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取excel intent
     * @param uri
     * @return
     */
    public static Intent getExcelFileIntent(Uri uri ) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取Ppt intent
     * @param uri
     * @return
     */
    public static Intent getPptFileIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }



}
