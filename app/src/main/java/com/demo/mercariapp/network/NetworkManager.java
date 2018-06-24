package com.demo.mercariapp.network;

import android.os.Handler;

import java.util.HashMap;

/**
 * This class is used for network operations.
 */
public final class NetworkManager {
    // Timeout value in milliseconds.
    private static final int TIMEOUT = 5 * 1000;

    private NetworkManager() {
    }

    /**
     * This method is to get the Category list from server.
     *
     * @param tag
     * @param handler
     */
    public static void getCategoryList(String tag, Handler handler) {
        String url = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/master.json";
        NetworkHandler.get(tag, url, handler, NetworkConstant.MessageState.CATEGORY_LIST_SUCCESS,
                NetworkConstant.MessageState.CATEGORY_LIST_FAIL, TIMEOUT);
    }

    /**
     * This method is to get the Data list from server.
     *
     * @param tag
     * @param url
     * @param handler
     */
    public static void getData(String tag, String url, Handler handler) {
        NetworkHandler.get(tag, url, handler, NetworkConstant.MessageState.DATA_LIST_SUCCESS,
                NetworkConstant.MessageState.DATA_LIST_FAIL, TIMEOUT);
    }

    public static void sendError(String tag, HashMap<String, String> params,
                                 Handler handler) {
        String url = ""; //Add url here.
        NetworkHandler.post(tag, url, params, handler,
                NetworkConstant.MessageState.SEND_ERROR_SUCCESS,
                NetworkConstant.MessageState.SEND_ERROR_FAIL, TIMEOUT);
    }
}