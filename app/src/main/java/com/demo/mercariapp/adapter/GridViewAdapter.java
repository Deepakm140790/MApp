package com.demo.mercariapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.mercariapp.R;
import com.demo.mercariapp.model.DataModel;
import com.demo.mercariapp.network.GlideLibHelper;

import java.util.ArrayList;

/**
 *This class is a adapter for the Recycler grid view.
 */
public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<DataModel> mData;

    public GridViewAdapter(Context context, ArrayList<DataModel> data) {
        mContext = context;
        mData = data;
    }

    /**
     * Holds the view objects.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView comment;
        private TextView like;
        private TextView price;
        private ImageView photo;
        private ImageView tag;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            comment = (TextView) view.findViewById(R.id.tv_comment);
            like = (TextView) view.findViewById(R.id.tv_like);
            price = (TextView) view.findViewById(R.id.tv_price);
            photo = (ImageView) view.findViewById(R.id.iv_photo);
            tag = (ImageView) view.findViewById(R.id.iv_tag);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        DataModel dataModel = mData.get(position);
        myViewHolder.name.setText(dataModel.getName());
        myViewHolder.comment.setText(String.valueOf(dataModel.getComments()));
        myViewHolder.like.setText(String.valueOf(dataModel.getlikes()));
        myViewHolder.price.setText("$ " + dataModel.getPrice());
        if (dataModel.getStatus().equals("on_sale")) {
            myViewHolder.tag.setVisibility(View.GONE);
        } else {
            myViewHolder.tag.setVisibility(View.VISIBLE);
            myViewHolder.tag.setImageResource(R.mipmap.badge_soldout);
        }

        // Network call to download a image from server.
        if (mContext != null && dataModel.getPhotoUrl() != null && !dataModel.getPhotoUrl().isEmpty()) {
            GlideLibHelper.downloadImage(mContext, dataModel.getPhotoUrl(), myViewHolder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Interface is used to send the onClick events to client.
     */
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * This class implements RecyclerView OnItemTouchListener.
     */
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
