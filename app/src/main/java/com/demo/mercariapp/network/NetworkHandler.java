package com.demo.mercariapp.network;

import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.mercariapp.Util.PrintLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class helps to make the network call by using the Volley.
 */
public final class NetworkHandler {

    private NetworkHandler() {
    }

    private static final String TAG = "NetworkHandler";

    /**
     * Method is used to get the data from server.
     * @param tag
     * @param url
     * @param handler
     * @param successId
     * @param failureId
     * @param timeOut
     */
    public static void get(String tag, String url, Handler handler, int successId,
                           int failureId, int timeOut) {
        PrintLog.d(TAG, "get() url --> " + url);
        VolleySingleton instance = VolleySingleton.getInstance();
        if (instance == null) {
            PrintLog.e(TAG, "VolleySingleton.getInstance() is null --> ");
            return;
        }
        instance.cancelPendingRequests(tag);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                ResponseListener.<JSONArray>createGenericReqSuccessListener(handler, successId),
                ResponseListener.createErrorListener(handler, failureId));
        jsonArrayRequest.setShouldCache(false);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 1, 1.0f));
        instance.addToRequestQueue(jsonArrayRequest, tag);
    }

    /**
     * Method is used to post the data to server.
     * @param tag
     * @param url
     * @param params
     * @param handler
     * @param successId
     * @param failureId
     * @param timeOut
     */
    public static void post(String tag, String url, HashMap<String, String> params,
                            Handler handler, int successId, int failureId, int timeOut) {
        PrintLog.d(TAG, "post() url --> " + url);
        VolleySingleton.getInstance().cancelPendingRequests(tag);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                ResponseListener.<JSONObject>createGenericReqSuccessListener(
                        handler, successId),
                ResponseListener.createErrorListener(handler,
                        failureId));
        jsObjRequest.setShouldCache(false);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 1, 1.0f));
        VolleySingleton.getInstance().addToRequestQueue(jsObjRequest, tag);
    }
}
