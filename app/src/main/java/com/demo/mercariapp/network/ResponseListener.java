package com.demo.mercariapp.network;

import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demo.mercariapp.util.PrintLog;

/**
 * This class is Generic class which helps to receive the result object which
 * comes from the server.
 */
public final class ResponseListener<T> {
    private static final String TAG = "ResponseListener";

    private ResponseListener() {
    }

    /**
     * This listener receives the success response which comes from the server
     * and sends the response to the client.
     *
     * @param handler
     * @param messageId
     * @param <T>
     * @return
     */
    public static <T> Response.Listener<T> createGenericReqSuccessListener(
            final Handler handler, final int messageId) {
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                PrintLog.v(TAG, " volley response -- " + response.toString());
                Message msg = new Message();
                msg.arg1 = messageId;
                msg.obj = response;
                handler.sendMessage(msg);
            }
        };
    }

    /**
     * This listener receives the error response which comes from the server
     * and sends the response to the client.
     *
     * @param handler
     * @param messageId
     * @return
     */
    public static Response.ErrorListener createErrorListener(
            final Handler handler, final int messageId) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = new Message();
                msg.arg1 = messageId;
                msg.obj = error;
                handler.sendMessage(msg);
                PrintLog.e(TAG, "Error in response " + error);
            }
        };
    }
}