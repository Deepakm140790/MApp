package com.demo.mercariapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.demo.mercariapp.R;
import com.demo.mercariapp.adapter.GridViewAdapter;
import com.demo.mercariapp.model.DataModel;
import com.demo.mercariapp.network.NetworkConstant;
import com.demo.mercariapp.network.NetworkManager;
import com.demo.mercariapp.network.VolleyErrorHelper;
import com.demo.mercariapp.ui.activity.MainActivity;
import com.demo.mercariapp.util.PrintLog;
import com.demo.mercariapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AllFragment extends Fragment implements MainActivity.MainActivityInteractionListener,
        GridViewAdapter.ClickListener {
    private static final String TAG = "AllFragment";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private static final String DTAG = "DataTagAll";
    private ArrayList<DataModel> mDataArray = new ArrayList<>();
    private ProgressBar mProgressBar;

    public AllFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PrintLog.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PrintLog.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_all);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_all);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new GridViewAdapter.RecyclerTouchListener(mContext, mRecyclerView, this));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.grid_item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void showProgressBar() {
        if (mProgressBar == null)
            return;
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (mProgressBar == null)
            return;
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        PrintLog.d(TAG, "onStart()");
        super.onStart();
        showProgressBar();
        if (mContext instanceof MainActivity) {
            String data = ((MainActivity) mContext).getData(this);
            if (data != null) {
                NetworkManager.getData(DTAG, data, mHandler);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        PrintLog.d(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        if (mRecyclerView == null)
            return;

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PrintLog.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        PrintLog.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        PrintLog.d(TAG, "onStop()");
        super.onStop();
        hideProgressBar();
    }

    @Override
    public void onAttach(Context context) {
        PrintLog.d(TAG, "onAttach()");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        PrintLog.d(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void loadData(String dataUrl) {
        PrintLog.d(TAG, "loadData -- " + dataUrl);
        if (this.isVisible())
            NetworkManager.getData(DTAG, dataUrl, mHandler);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case NetworkConstant.MessageState.DATA_LIST_SUCCESS:
                    hideProgressBar();
                    parseDataResult((JSONArray) msg.obj);
                    break;

                case NetworkConstant.MessageState.DATA_LIST_FAIL:
                    hideProgressBar();
                    Util.showToast(mContext, VolleyErrorHelper.getErrorType(msg));
                    break;
            }
        }
    };

    private void parseDataResult(JSONArray array) {
        PrintLog.d(TAG, "parseDataResult() obj - " + array);
        if (array == null || array.length() == 0) {
            return;
        }
        mDataArray.clear();
        for (int i = 0; i < array.length(); i++) {
            try {
                DataModel dataModel = new DataModel();
                dataModel.setId(array.getJSONObject(i).getString("id"));
                dataModel.setName(array.getJSONObject(i).getString("name"));
                dataModel.setComments(array.getJSONObject(i).getInt("num_comments"));
                dataModel.setlikes(array.getJSONObject(i).getInt("num_likes"));
                dataModel.setPrice(array.getJSONObject(i).getInt("price"));
                dataModel.setStatus(array.getJSONObject(i).getString("status"));
                dataModel.setPhotoUrl(array.getJSONObject(i).getString("photo"));
                mDataArray.add(dataModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (mContext == null) {
            PrintLog.e(TAG, "context is null");
            return;
        }
        GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mDataArray);
        mRecyclerView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onClick(View view, int position) {
        Util.showToast(mContext, "GridView item clicked and index position : " + position);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    /**
     * This class is used for view Decoration.
     */
    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}