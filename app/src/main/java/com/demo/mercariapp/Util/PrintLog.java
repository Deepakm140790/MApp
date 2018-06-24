package com.demo.mercariapp.Util;

import android.util.Log;

public final class PrintLog {
    private static final String APP_NAME = "MercariApp -- ";

    private PrintLog() {
    }

    public static void d(String tag, final String msg) {
        Log.d(APP_NAME + tag, msg);
    }

    public static void e(String tag, final String msg) {
        Log.e(APP_NAME + tag, msg);
    }

    public static void v(String tag, final String msg) {
        Log.v(APP_NAME + tag, msg);
    }

    public static void w(String tag, final String msg) {
        Log.e(APP_NAME + tag, msg);
    }
}
