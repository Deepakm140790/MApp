package com.demo.mercariapp.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.demo.mercariapp.R;

/**
 * This class create a custom view which looks like floating action bar.
 */
public class CustomFab extends RelativeLayout {
    private LayoutInflater mInflater;

    public CustomFab(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init(null);
    }

    public CustomFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public CustomFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mInflater.inflate(R.layout.fab_layout, this, true);
    }
}
