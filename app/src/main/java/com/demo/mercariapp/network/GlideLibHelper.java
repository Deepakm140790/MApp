package com.demo.mercariapp.network;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Class used to download a image from the server.
 */
public final class GlideLibHelper {

    private GlideLibHelper() {
    }

    /**
     * This method used to download the image from server and also set the
     * downloaded photo to give ImageView.
     * @param context
     * @param url
     * @param imageView
     */
    public static void downloadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
