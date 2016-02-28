package com.equidais.mybeacon.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.equidais.mybeacon.model.VisitEntriesResult;

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
public class DetailFragment extends BaseFragment  implements View.OnClickListener{


    boolean mIsFinish= false;

    boolean mIsLoaded = false;
    int mTopTabNo;
    TextView mTxtTab1;
    TextView mTxtTab2;
    TextView mTxtTab3;
    List<VisitEntriesResult> mListData = new ArrayList<VisitEntriesResult>();
    List<VisitEntriesResult> mListShow = new ArrayList<VisitEntriesResult>();
    ListView mListView;
    DetailListAdapter mAdapter;
    View mEmptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTxtTab1 = (TextView)mRootView.findViewById(R.id.txt_top_tab1);
        mTxtTab2 = (TextView)mRootView.findViewById(R.id.txt_top_tab2);
        mTxtTab3 = (TextView)mRootView.findViewById(R.id.txt_top_tab3);
        mTxtTab1.setOnClickListener(this);
        mTxtTab2.setOnClickListener(this);
        mTxtTab3.setOnClickListener(this);
        mListView = (ListView)mRootView.findViewById(R.id.list_view);
        mEmptyView = mRootView.findViewById(R.id.txt_empty);
        mIsFinish = false;
        initListView();
        if (!mIsLoaded) {
            setTopTab(0);
        }else{
            setTopTab(mTopTabNo);
        }
        loadData();
        if (!mIsLoaded){
            mIsLoaded = true;
        }
        return mRootView;
    }
    private void initListView(){



        mAdapter = new DetailListAdapter(getActivity(), mListShow);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);

    }

    private void setTopTab(int pos){
        mTopTabNo = pos;
        mTxtTab1.setBackgroundResource(R.drawable.back_tab_left);
        mTxtTab2.setBackgroundResource(R.color.transparent);
        mTxtTab3.setBackgroundResource(R.drawable.back_tab_right);
        mTxtTab1.setTextColor(getResources().getColor(R.color.blueTextColor));
        mTxtTab2.setTextColor(getResources().getColor(R.color.blueTextColor));
        mTxtTab3.setTextColor(getResources().getColor(R.color.blueTextColor));
        if (pos == 0){
            mTxtTab1.setBackgroundResource(R.drawable.back_tab_left_select);
            mTxtTab1.setTextColor(getResources().getColor(R.color.white));
        }else if (pos == 1){
            mTxtTab2.setBackgroundResource(R.color.tabSelectColor);
            mTxtTab2.setTextColor(getResources().getColor(R.color.white));
        }else if (pos == 2){
            mTxtTab3.setBackgroundResource(R.drawable.back_tab_right_select);
            mTxtTab3.setTextColor(getResources().getColor(R.color.white));
        }
        showData();
    }

    private void showData(){
        if (mTopTabNo == 0){
            mListShow.clear();
            for (int i = 0; i<mListData.size(); i++){
                VisitEntriesResult entry = mListData.get(i);
                if (GlobalFunc.isSameWeek(entry.TimeIn)) {
                    mListShow.add(mListData.get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
        }else if (mTopTabNo == 1){
            mListShow.clear();
            for (int i = 0; i<mListData.size(); i++){
                VisitEntriesResult entry = mListData.get(i);
                if (GlobalFunc.isSameMonth(entry.TimeIn)) {
                    mListShow.add(mListData.get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
        }else{
            mListShow.clear();
            for (int i = 0; i<mListData.size(); i++){
                mListShow.add(mListData.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void loadData(){
        Map<String, Object> map = new HashMap<>();
        map.put("userid", LocalData.getUserID(getActivity()));
        ApiClient.getApiClient().getVisitEntries(map, new Callback<List<VisitEntriesResult>>() {
            @Override
            public void success(List<VisitEntriesResult> visitEntriesResults, Response response) {
                mListData = visitEntriesResults;
                if (!mIsFinish){
                    showData();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        mIsFinish = true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_top_tab1){
            setTopTab(0);
        }else if (view.getId() == R.id.txt_top_tab2){
            setTopTab(1);
        }else if (view.getId() == R.id.txt_top_tab3){
            setTopTab(2);
        }
    }

}
