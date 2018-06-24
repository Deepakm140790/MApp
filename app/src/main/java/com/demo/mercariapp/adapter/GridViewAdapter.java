package com.demo.mercariapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.mercariapp.R;
import com.demo.mercariapp.model.DataModel;
import com.demo.mercariapp.network.GlideLibHelper;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<DataModel> mData;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<DataModel> data) {
        super(context, layoutResourceId, data);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.comment = (TextView) view.findViewById(R.id.tv_comment);
            holder.like = (TextView) view.findViewById(R.id.tv_like);
            holder.price = (TextView) view.findViewById(R.id.tv_price);
            holder.photo = (ImageView) view.findViewById(R.id.iv_photo);
            holder.tag = (ImageView) view.findViewById(R.id.iv_tag);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataModel dataModel = mData.get(position);
        holder.name.setText(dataModel.getName());
        holder.comment.setText(String.valueOf(dataModel.getComments()));
        holder.like.setText(String.valueOf(dataModel.getlikes()));
        holder.price.setText("$ " + dataModel.getPrice());
        if (dataModel.getStatus().equals("on_sale")) {
            holder.tag.setVisibility(View.GONE);
        } else {
            holder.tag.setVisibility(View.VISIBLE);
            holder.tag.setImageResource(R.mipmap.badge_soldout);
        }

        // Network call to download a image from server.
        if (mContext != null && dataModel.getPhotoUrl() != null && !dataModel.getPhotoUrl().isEmpty()) {
            GlideLibHelper.downloadImage(mContext, dataModel.getPhotoUrl(), holder.photo);
        }
        return view;
    }

    static class ViewHolder {
        private TextView name;
        private TextView comment;
        private TextView like;
        private TextView price;
        private ImageView photo;
        private ImageView tag;
    }
}
