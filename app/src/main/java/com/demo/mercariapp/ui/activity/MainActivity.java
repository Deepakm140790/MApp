package com.demo.mercariapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.mercariapp.R;
import com.demo.mercariapp.Util.ExceptionReport;
import com.demo.mercariapp.Util.PrintLog;
import com.demo.mercariapp.Util.Util;
import com.demo.mercariapp.network.NetworkConstant;
import com.demo.mercariapp.network.NetworkManager;
import com.demo.mercariapp.network.VolleyErrorHelper;
import com.demo.mercariapp.ui.customview.CustomFab;
import com.demo.mercariapp.ui.fragment.AllFragment;
import com.demo.mercariapp.ui.fragment.MenFragment;
import com.demo.mercariapp.ui.fragment.WomenFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String CTAG = "CategoryTag";

    private MenFragment mMenFragment;
    private AllFragment mAllFragment;
    private WomenFragment mWomenFragment;

    private String mMenData = null;
    private String mAllData = null;
    private String mWomenData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set custom action bar
        Util.setActionBar(this, R.layout.custom_actionbar_layout);
        setContentView(R.layout.activity_main);

        init();

        // Network call to get the data from server.
        NetworkManager.getCategoryList(CTAG, mHandler);
    }

    private void init() {
        CustomFab fab = (CustomFab) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case NetworkConstant.MessageState.CATEGORY_LIST_SUCCESS:
                    parseCategoryResult((JSONArray) msg.obj);
                    break;

                case NetworkConstant.MessageState.CATEGORY_LIST_FAIL:
                    Util.showToast(getApplication(), VolleyErrorHelper.getErrorType(msg));
                    break;
            }
        }
    };

    private void parseCategoryResult(JSONArray array) {
        PrintLog.d(TAG, "parseCategoryResult() obj - " + array);
        if (array == null || array.length() == 0) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                String data = object.getString("data");
                if (name.equals("All")) {
                    mAllFragment.loadData(data);
                    mAllData = data;
                } else if (name.equals("Men")) {
                    mMenFragment.loadData(data);
                    mMenData = data;
                } else if (name.equals("Woman")) {
                    mWomenFragment.loadData(data);
                    mWomenData = data;
                }
            } catch (JSONException e) {
                ExceptionReport.send(e, mHandler);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAllFragment = new AllFragment();
        mMenFragment = new MenFragment();
        mWomenFragment = new WomenFragment();

        adapter.addFragment(mMenFragment, getResources().getString(R.string.tab_tittle_men));
        adapter.addFragment(mAllFragment, getResources().getString(R.string.tab_tittle_all));
        adapter.addFragment(mWomenFragment, getResources().getString(R.string.tab_tittle_women));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Util.showToast(getApplicationContext(), "Floating action clicked");
                break;

            default:
                break;
        }
    }

    public String getData(Fragment fragment) {
        if (fragment instanceof MenFragment) {
            return mMenData;
        } else if (fragment instanceof AllFragment) {
            return mAllData;
        } else if (fragment instanceof WomenFragment) {
            return mWomenData;
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("menData", mMenData);
        outState.putString("allData", mAllData);
        outState.putString("womenData", mWomenData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMenData = savedInstanceState.getString("menData");
        mAllData = savedInstanceState.getString("allData");
        mWomenData = savedInstanceState.getString("womenData");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface MainActivityInteractionListener {
        void loadData(String dataUrl);
    }
}
