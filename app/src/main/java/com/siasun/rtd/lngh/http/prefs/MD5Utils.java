package com.siasun.rtd.lngh.http.prefs;

import java.security.MessageDigest;

public class MD5Utils {
    public MD5Utils() {
    }

    public static final String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] input = s.getBytes();
            MessageDigest mdInstance = MessageDigest.getInstance("MD5");
            mdInstance.update(input);
            byte[] md = mdInstance.digest();
            int length = md.length;
            char[] str = new char[length * 2];
            int i = 0;

            for(int j = 0; j < length; ++j) {
                byte b = md[j];
                str[i++] = hexDigits[b >>> 4 & 15];
                str[i++] = hexDigits[b & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }
}
