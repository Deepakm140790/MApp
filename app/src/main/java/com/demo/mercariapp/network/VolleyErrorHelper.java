package com.demo.mercariapp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.demo.mercariapp.util.PrintLog;

/**
 * Class returns appropriate message which is to be displayed to the user
 * against the specified error object.
 */
public final class VolleyErrorHelper {
    private static final String TAG = "VolleyErrorHelper";
    private static String connectionTimeOut = "Connection timeout. Please check the internet";
    private static String networkError = "No internet connection";
    private static String serverError = "Unable to connect server. Please try later";
    private static String defError = "Unknown error. Please try later";

    private VolleyErrorHelper() {
    }

    /**
     * Method returns what type of error occurred.
     */
    public static String getErrorType(Object error) {
        String whatError = "";
        try {
            if (error instanceof TimeoutError) {
                PrintLog.e(TAG, "getMessage result msg-->" + "time out");
                whatError = connectionTimeOut;
            } else if (error instanceof ServerError
                    || error instanceof AuthFailureError) {
                PrintLog.e(TAG, "getMessage result msg-->" + "ServerError");
                whatError = serverError;
            } else if (error instanceof NetworkError
                    || error instanceof NoConnectionError) {
                PrintLog.e(TAG, "getMessage result msg-->" + "NetworkError");
                whatError = networkError;
            } else {
                whatError = defError;
            }
        } catch (Exception e) {
            PrintLog.e(TAG, "ExceptionReport " + e);
        }
        return whatError;
    }
}