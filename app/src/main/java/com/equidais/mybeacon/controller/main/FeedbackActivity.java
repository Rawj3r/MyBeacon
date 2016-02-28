package com.equidais.mybeacon.controller.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.equidais.mybeacon.model.LoginResult;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FeedbackActivity extends BaseActivity implements View.OnClickListener{


    boolean mHappy = true;
    EditText mEditFeedback;
    TextView mTxtHappy;
    TextView mTxtSad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.txt_happy).setOnClickListener(this);
        findViewById(R.id.txt_sad).setOnClickListener(this);
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.txt_submit).setOnClickListener(this);
        mEditFeedback = (EditText)findViewById(R.id.edit_text);
        mTxtHappy = (TextView)findViewById(R.id.txt_happy);
        mTxtSad = (TextView)findViewById(R.id.txt_sad);
        setHappy(true);

    }

    private void setHappy(boolean happy){
        mHappy = happy;
        if (mHappy) {
            mTxtHappy.setTextColor(getResources().getColor(R.color.white));
            mTxtSad.setTextColor(getResources().getColor(R.color.unSelectColor));
        }else{
            mTxtHappy.setTextColor(getResources().getColor(R.color.unSelectColor));
            mTxtSad.setTextColor(getResources().getColor(R.color.white));
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_happy){
            setHappy(true);
        }else if (view.getId() == R.id.txt_sad){
            setHappy(false);
        }else if (view.getId() == R.id.txt_cancel){
            finish();
        }else if (view.getId() == R.id.txt_submit){
            sendFeedback();
        }
    }
    ProgressDialog mProgressDlg;
    private void sendFeedback(){
        String feedback = mEditFeedback.getText().toString();
        if (feedback.equals("")){
            GlobalFunc.showAlertDialog(this, getResources().getString(R.string.warning),
                    getResources().getString(R.string.alert_input_feedback));
            return;
        }
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", LocalData.getUserID(this));
        hashMap.put("mood", mHappy);
        hashMap.put("message", feedback);
        mProgressDlg = GlobalFunc.showProgressDialog(this);
        ApiClient.getApiClient().addGymVisitFeedback(hashMap, new Callback<Integer>() {
            @Override
            public void success(Integer result, Response response) {
                mProgressDlg.dismiss();
                finish();
                /*
                if (result != null && result != 0){
                    finish();
                }else{
                    GlobalFunc.showAlertDialog(FeedbackActivity.this, getResources().getString(R.string.warning),
                            getResources().getString(R.string.alert_connect_failed));
                }
                */
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressDlg.dismiss();
                finish();
                /*
                GlobalFunc.showAlertDialog(FeedbackActivity.this, getResources().getString(R.string.warning),
                        getResources().getString(R.string.alert_connect_failed));
                        */
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
