package com.equidais.mybeacon.controller.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.equidais.mybeacon.model.MessageResult;
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
public class MessagesFragment extends BaseFragment {


    boolean mIsFinish= false;


    List<MessageResult> mListData = new ArrayList<MessageResult>();

    ListView mListView;
    MessageListAdapter mAdapter;
    View mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.fragment_messages, container, false);

        mListView = (ListView)mRootView.findViewById(R.id.list_view);
        mEmptyView = mRootView.findViewById(R.id.txt_empty);
        mIsFinish = false;
        initListView();
        loadData();
        return mRootView;
    }

    private void initListView(){

        mAdapter = new MessageListAdapter(getActivity(), mListData);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageResult message = mListData.get(i);
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.row_dlg_gym, null);
                final TextView txtGym = (TextView)promptView.findViewById(R.id.txt_gym);
                txtGym.setText("Gym: " + message.Gym);
                new AlertDialog.Builder(getActivity())
                        .setTitle(message.Title)
                        .setMessage(message.Message)
                        .setView(promptView)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

            }
        });
    }

    private void loadData(){
        Map<String, Object> map = new HashMap<>();
        map.put("userid", LocalData.getUserID(getActivity()));
        ApiClient.getApiClient().getMessageList(map, new Callback<List<MessageResult>>() {
            @Override
            public void success(List<MessageResult> list, Response response) {
                if (list != null){
                    mListData.clear();
                    for (int i = 0; i<list.size(); i++){
                        mListData.add(list.get(i));
                    }
                    /*
                    MessageResult result = new MessageResult();
                    result.Title = "Virgin Active";
                    result.Message = "Hi John\nYour new gym card is ready for collection";
                    result.Gym = "Virgin Active Menlyn";
                    mListData.add(result);
                    */
                    if (!mIsFinish) {
                        mAdapter.notifyDataSetChanged();
                    }
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

}
