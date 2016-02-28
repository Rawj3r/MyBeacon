package com.equidais.mybeacon.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.controller.common.BaseFragment;


/**
 * Created by daydreamer on 7/31/2015.
 */
public class ContactUsFragment extends BaseFragment {


    boolean mIsFinish= false;

    boolean mIsLoaded = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mRootView.findViewById(R.id.txt_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        mIsFinish = false;

        TextView txtContactUs= (TextView)mRootView.findViewById(R.id.txt_contact_us);
        txtContactUs.setText(Html.fromHtml(getResources().getString(R.string.contact_us)));
        return mRootView;
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        mIsFinish = true;
    }

}
