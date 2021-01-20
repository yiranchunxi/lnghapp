package com.siasun.rtd.lngh.http.prefs;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SystemUtils {

    public SystemUtils() {
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
        return tm.getDeviceId();
    }
}
