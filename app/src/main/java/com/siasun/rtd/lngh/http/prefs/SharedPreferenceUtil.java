package com.siasun.rtd.lngh.http.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public final class SharedPreferenceUtil {

    private static final String FILE_NAME="ssk";
    private static SharedPreferenceUtil mInstance;
    private String k="51b90c701924108f";
    private SharedPreferenceUtil(){}

    public static SharedPreferenceUtil getInstance(){
        if(mInstance == null){
            synchronized (SharedPreferenceUtil.class){
                if(mInstance == null){
                    mInstance = new SharedPreferenceUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 存入键值对
     * @param context
     * @param key
     * @param value
     */

    public void put(Context context, String key, String value){
        //判断类型
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, this.encryptData(value)).apply();
        editor.putString(key + key, this.encryptData(MD5Utils.MD5(value))).apply();
    }

    /**
     * 读取键的值，若无则返回默认值
     * @param context
     * @param key
     * @return
     */
    @Nullable
    public String get(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        try {
            String valueE = sharedPreferences.getString(key, "");
            String valueEN = sharedPreferences.getString(key + key, "");
            if (!"".equals(valueE) && !"".equals(valueEN)) {
                String valueD = this.decryptData(valueE);
                String valueDN = this.decryptData(valueEN);
                return this.check(valueD, valueDN) ? valueD : "";
            } else {
                return "";
            }
        } catch (Exception var6) {
            return "";
        }
    }

    public void clearConfig(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
        editor.remove(key + key).apply();
    }
    private String encryptData(String data) {
        String mEncryptData = PBOCDES.hex(PBOCDES.pboc3DESEncryptForLong(PBOCDES.parseBytes(this.k), data.getBytes(), true, true));
        return mEncryptData;
    }

    private String decryptData(String s) {
        byte[] deCode = PBOCDES.pboc3DESDecrypt(PBOCDES.parseBytes(this.k), PBOCDES.parseBytes(s));
        byte[] len = new byte[]{deCode[0], deCode[1]};
        int lenght = Integer.parseInt(StringUtils.bytesToHexString(len), 16);
        byte[] value = new byte[lenght];
        System.arraycopy(deCode, 2, value, 0, lenght);
        String data = new String(value);
        return data;
    }

    private boolean check(String value, String md5) {
        try {
            String vMD5 = MD5Utils.MD5(value);
            return md5.equalsIgnoreCase(vMD5);
        } catch (Exception var4) {
            return false;
        }
    }
}
