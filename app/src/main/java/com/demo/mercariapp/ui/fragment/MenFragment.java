package com.demo.mercariapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.demo.mercariapp.R;
import com.demo.mercariapp.Util.PrintLog;
import com.demo.mercariapp.Util.Util;
import com.demo.mercariapp.adapter.GridViewAdapter;
import com.demo.mercariapp.model.DataModel;
import com.demo.mercariapp.network.NetworkConstant;
import com.demo.mercariapp.network.NetworkManager;
import com.demo.mercariapp.network.VolleyErrorHelper;
import com.demo.mercariapp.ui.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MenFragment extends Fragment implements MainActivity.MainActivityInteractionListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MenFragment";
    private Context mContext;
    private GridView mGridView;
    private static final String DTAG = "DataTagMen";
    private ArrayList<DataModel> mDataArray = new ArrayList<>();
    private ProgressBar mProgressBar;

    public MenFragment() {
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
        View view = inflater.inflate(R.layout.fragment_men, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mGridView = (GridView) view.findViewById(R.id.gv_men);
        mGridView.setOnItemClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_men);
    }

    private void showProgressBar() {
        if (mProgressBar == null)
            return;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (mProgressBar == null)
            return;
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        PrintLog.d(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);

        if (mGridView == null)
            return;

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(2);
        }
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
        GridViewAdapter adapter = new GridViewAdapter(mContext, R.layout.gridview_item_layout, mDataArray);
        mGridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PrintLog.d(TAG, "onItemClick");

        // Get the GridView selected/clicked item text
        switch (view.getId()) {
            case R.id.tv_like:
                Util.showToast(mContext, "Like view clicked");
                break;

            case R.id.tv_comment:
                Util.showToast(mContext, "Comment view clicked");
                break;

            default:
                DataModel dataModel = mDataArray.get(position);
                // Display the selected/clicked item text and position on TextView
                Util.showToast(mContext, "GridView item clicked :" + dataModel.getName() +
                        "\nAt index position : " + position);
                break;
        }
    }
}