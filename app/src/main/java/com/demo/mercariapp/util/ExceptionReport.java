package com.demo.mercariapp.util;

import android.os.Handler;

import com.demo.mercariapp.network.NetworkManager;

import java.util.HashMap;

/**
 * This class is used to send the ExceptionReport report to server.
 */
public final class ExceptionReport {
    private static final String TAG = "ExceptionReport";

    private ExceptionReport() {
    }

    /**
     * This method is used to send the ExceptionReport report to server.
     * @param error
     * @param handler
     */
    public static void send(Exception error, Handler handler) {
        PrintLog.e(TAG, "ExceptionReport " + error);
        HashMap<String, String> params = new HashMap<>(); // Add error message to params.
        NetworkManager.sendError(TAG, params, handler);
    }
}
