package com.equidais.mybeacon.controller.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.MainApplication;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.equidais.mybeacon.model.VisitSummaryResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by daydreamer on 7/31/2015.
 */
public class SummaryFragment extends BaseFragment implements View.OnClickListener {


    private static final String LOG = "test data";
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    boolean mIsFinish = false;

    private List<GymVisits> gymVisitses = new ArrayList<>();

    boolean mIsLoaded = false;

    int mTopTabNo;

    Context context;

    TextView mTxtTab1;
    TextView mTxtTab2;
    TextView mTxtTab3;

    TextView mTxtVisits;
    TextView mTxtAverageVisitDuration;
    TextView mTxtCurrentVisitDruation;
    TextView mTxtLastVisitDate;

    List<VisitSummaryResult> mListData = new ArrayList<VisitSummaryResult>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.fragment_summary, container, false);
        mTxtTab1 = (TextView) mRootView.findViewById(R.id.txt_top_tab1);
        mTxtTab2 = (TextView) mRootView.findViewById(R.id.txt_top_tab2);
        mTxtTab3 = (TextView) mRootView.findViewById(R.id.txt_top_tab3);
        mTxtVisits = (TextView) mRootView.findViewById(R.id.txt_visits);
        mTxtAverageVisitDuration = (TextView) mRootView.findViewById(R.id.txt_average_visit_duration);
        mTxtCurrentVisitDruation = (TextView) mRootView.findViewById(R.id.txt_current_visit_duration);
        mTxtLastVisitDate = (TextView) mRootView.findViewById(R.id.txt_last_visit_date);
        mTxtTab1.setOnClickListener(this);
        mTxtTab2.setOnClickListener(this);
        mTxtTab3.setOnClickListener(this);
        mIsFinish = false;

        if (!mIsLoaded) {
            mListData.add(new VisitSummaryResult());
            mListData.add(new VisitSummaryResult());
            mListData.add(new VisitSummaryResult());
            setTopTab(0);
        } else {
            setTopTab(mTopTabNo);
        }

        if (!mIsLoaded) {
            mIsLoaded = true;
        }
        showState();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("changeState");
        getActivity().registerReceiver(mMsgRecevier, intentFilter);
        mHandler.sendEmptyMessageDelayed(0, 1000);
        sendData("3");
        return mRootView;
    }


    private void setTopTab(int pos) {
        mTopTabNo = pos;
        mTxtTab1.setBackgroundResource(R.drawable.back_tab_left);
        mTxtTab2.setBackgroundResource(R.color.transparent);
        mTxtTab3.setBackgroundResource(R.drawable.back_tab_right);
        mTxtTab1.setTextColor(getResources().getColor(R.color.blueTextColor));
        mTxtTab2.setTextColor(getResources().getColor(R.color.blueTextColor));
        mTxtTab3.setTextColor(getResources().getColor(R.color.blueTextColor));
        if (pos == 0) {
            mTxtTab1.setBackgroundResource(R.drawable.back_tab_left_select);
            mTxtTab1.setTextColor(getResources().getColor(R.color.white));
        } else if (pos == 1) {
            mTxtTab2.setBackgroundResource(R.color.tabSelectColor);
            mTxtTab2.setTextColor(getResources().getColor(R.color.white));
        } else if (pos == 2) {
            mTxtTab3.setBackgroundResource(R.drawable.back_tab_right_select);
            mTxtTab3.setTextColor(getResources().getColor(R.color.white));
        }
        loadData(mTopTabNo);
    }

    private void showData(int tabNo) {
        VisitSummaryResult visitSummaryResult = mListData.get(tabNo);
        if (visitSummaryResult.VisitCount == null) {
            mTxtVisits.setText(R.string.na);
        } else {
            mTxtVisits.setText(String.valueOf(visitSummaryResult.VisitCount.longValue()));
        }
        if (visitSummaryResult.AvgVisitDuration == null) {
            mTxtAverageVisitDuration.setText(R.string.na);
        } else {
            mTxtAverageVisitDuration.setText(GlobalFunc.getDateFormatStringFromDuration(visitSummaryResult.AvgVisitDuration));
        }
        String lastVisitDate = GlobalFunc.getDateString(visitSummaryResult.LastVisitDate);
        if (lastVisitDate.equals("")) {
            mTxtLastVisitDate.setText(R.string.na);
        } else {
            mTxtLastVisitDate.setText(lastVisitDate);
        }
    }

    private void loadData(final int tabNo) {
        showData(tabNo);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", LocalData.getUserID(getActivity()));
        map.put("period", tabNo);
        ApiClient.getApiClient().getVisitSummary(map, new Callback<List<VisitSummaryResult>>() {
            @Override
            public void success(List<VisitSummaryResult> visitSummaryResults, Response response) {
                if (visitSummaryResults != null && visitSummaryResults.size() > 0) {
                    VisitSummaryResult result = visitSummaryResults.get(0);
                    VisitSummaryResult visitSummaryResult = mListData.get(tabNo);
                    visitSummaryResult.VisitCount = result.VisitCount;
                    visitSummaryResult.AvgVisitDuration = result.AvgVisitDuration;
                    visitSummaryResult.LastVisitDate = result.LastVisitDate;
                    if (!mIsFinish && tabNo == mTopTabNo) {
                        showData(tabNo);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMsgRecevier);
        mIsFinish = true;
        mHandler.removeMessages(0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_top_tab1) {
            setTopTab(0);
        } else if (view.getId() == R.id.txt_top_tab2) {
            setTopTab(1);
        } else if (view.getId() == R.id.txt_top_tab3) {
            setTopTab(2);
        }
    }

    public BroadcastReceiver mMsgRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("changeState")) {
                showState();
            }
        }
    };

    private void showState() {
        MainApplication application = (MainApplication) getActivity().getApplication();
        if (application.mState == MainApplication.STATE_INIT) {
            mTxtCurrentVisitDruation.setText(R.string.na);
        } else if (application.mState == MainApplication.STATE_ENTER_DOOR) {
            mTxtCurrentVisitDruation.setText(R.string.na);
        } else if (application.mState == MainApplication.STATE_IN_ROOM) {
            mTxtCurrentVisitDruation.setText(GlobalFunc.getDurationFromInTime(application.mInTime));
        } else if (application.mState == MainApplication.STATE_OUT_ROOM_ENTER_DOOR) {
            mTxtCurrentVisitDruation.setText(R.string.na);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showState();
            mHandler.sendEmptyMessageDelayed(0, 500);
        }
    };

    /*SharedPreferences sharedPreferences = */

    public void sendData(final String username) {

        final StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.MAIN_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG, "`eskom  Response: " + response.toString());
                        String res = response.toString();
                        Log.d(LOG, "response fro the server " + res);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                GymVisits gymVisits = new GymVisits();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String gymId = jsonObject.getString("id");
                                String timeIn = jsonObject.getString("Amount");
                                String timeOut = jsonObject.getString("Ref");

                                gymVisits.setID(gymId);
                                gymVisits.setTIME_ID(timeIn);
                                gymVisits.setTIME_OUT(timeOut);



                            } catch (JSONException e) {
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, "Registration Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                Log.e(LOG, "Values from the device " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(strReq);
    }

}

