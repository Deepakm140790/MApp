package com.demo.mercariapp.util;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.Toolbar;

public final class Util {
    private Util() {
    }

    /**
     * This method show the toast message to the user.
     * Purpose of this method is in the feature we need to convert showing toast message to Dialog message.
     * In this case we need to change the one place. It consumes a lot of time.
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to set the custom action bar.
     * @param appCompatActivity
     * @param layout
     * @return
     */
    public static View setActionBar(AppCompatActivity appCompatActivity, int layout) {
        appCompatActivity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        appCompatActivity.getSupportActionBar().setCustomView(layout);
        appCompatActivity.getSupportActionBar().setHomeButtonEnabled(false);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        appCompatActivity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = appCompatActivity.getSupportActionBar().getCustomView();

        //Below code is used to remove the left and right actionbar padding.
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        appCompatActivity.getSupportActionBar().setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return view;
    }
}
