package com.equidais.mybeacon.controller.main;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalConst;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.MainApplication;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.equidais.mybeacon.controller.common.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    BaseFragment mSummaryFrag;
    BaseFragment mDetailFrag;
    BaseFragment mMessagesFrag;
    BaseFragment mContactUsFrag;
    NewSummaryFragment newSummaryFragment;
    GoogleCloudMessaging gcm;

    int mTabNo = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.tab3).setOnClickListener(this);
        findViewById(R.id.tab4).setOnClickListener(this);
        setTab(0);

       // getPush();
    }

    private void getPush(){

        Log.e("Push", "GCM registration success" + GlobalFunc.getPushId(this));
        if (GlobalFunc.getPushState(this) == 0) {
            if (checkPlayServices()) {
                updateRegistration();
            } else {
                Log.e("Push", "No valid Google Play Services APK found.");
            }
        }else if (GlobalFunc.getPushState(this) == 1){
            sendGCMIdToServer();
        }


    }

    private void updateRegistration() {
        gcm = GoogleCloudMessaging.getInstance(this);
        registerInBackground();

    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    String regid = gcm.register(GlobalConst.GCM_SENDER_ID);
                    GlobalFunc.savePushId(MainActivity.this, regid);
                    GlobalFunc.savePushState(MainActivity.this, 1);
                    Log.e("Push", "GCM registration success" + regid);
                    return "Device registered, registration ID=" + regid;
                } catch (final IOException ex) {
                    Log.e("Push", "GCM registration failed.", ex);
                    return "Cannot register with GCM:" + ex.getMessage();

                }
            }

            @Override
            protected void onPostExecute(final String msg) {
                if (GlobalFunc.getPushState(MainActivity.this) == 1) {
                    sendGCMIdToServer();
                }

            }
        }.execute(null, null, null);
    }

    private void sendGCMIdToServer(){
        //to do

        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", LocalData.getUserID(this));
        map.put("pushid", GlobalFunc.getPushId(this));
        map.put("deviceudid",  GlobalFunc.getDeviceUDID(this));
        ApiClient.getApiClient().registerPushId(map, new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                Log.e("push reg", "success");

                GlobalFunc.savePushState(MainActivity.this, 2);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("push reg", "fail");

            }
        });



    }

    private boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("Push", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void setTab(int pos){
        ImageView imgTab1 = (ImageView)findViewById(R.id.img_tab1);
        ImageView imgTab2 = (ImageView)findViewById(R.id.img_tab2);
        ImageView imgTab3 = (ImageView)findViewById(R.id.img_tab3);
        ImageView imgTab4 = (ImageView)findViewById(R.id.img_tab4);
        TextView txtTab1 = (TextView)findViewById(R.id.txt_tab1);
        TextView txtTab2 = (TextView)findViewById(R.id.txt_tab2);
        TextView txtTab3 = (TextView)findViewById(R.id.txt_tab3);
        TextView txtTab4 = (TextView)findViewById(R.id.txt_tab4);
        imgTab1.setColorFilter(getResources().getColor(R.color.tabColor));
        imgTab2.setColorFilter(getResources().getColor(R.color.tabColor));
        imgTab3.setColorFilter(getResources().getColor(R.color.tabColor));
        imgTab4.setColorFilter(getResources().getColor(R.color.tabColor));
        txtTab1.setTextColor(getResources().getColor(R.color.tabColor));
        txtTab2.setTextColor(getResources().getColor(R.color.tabColor));
        txtTab3.setTextColor(getResources().getColor(R.color.tabColor));
        txtTab4.setTextColor(getResources().getColor(R.color.tabColor));
        mTabNo = pos;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mTabNo == 0){
            imgTab1.setColorFilter(getResources().getColor(R.color.tabSelectColor));
            txtTab1.setTextColor(getResources().getColor(R.color.tabSelectColor));
            if (mSummaryFrag == null) {
                mSummaryFrag = new SummaryFragment();
            }

            if (newSummaryFragment == null){
                newSummaryFragment = new NewSummaryFragment();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mSummaryFrag)
                    .commit();
        }else if (mTabNo == 1){
            imgTab2.setColorFilter(getResources().getColor(R.color.tabSelectColor));
            txtTab2.setTextColor(getResources().getColor(R.color.tabSelectColor));
            if (mDetailFrag == null) {
                mDetailFrag = new DetailFragment();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mDetailFrag)
                    .commit();
        }else if (mTabNo == 2){
            imgTab3.setColorFilter(getResources().getColor(R.color.tabSelectColor));
            txtTab3.setTextColor(getResources().getColor(R.color.tabSelectColor));
            if (mMessagesFrag == null) {
                mMessagesFrag = new MessagesFragment();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mMessagesFrag)
                    .commit();
        }else if (mTabNo == 3){
            imgTab4.setColorFilter(getResources().getColor(R.color.tabSelectColor));
            txtTab4.setTextColor(getResources().getColor(R.color.tabSelectColor));
            if (mContactUsFrag == null) {
                mContactUsFrag = new ContactUsFragment();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mContactUsFrag)
                    .commit();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tab1){
            setTab(0);
        }else if (view.getId() == R.id.tab2){
            setTab(1);
        }else if (view.getId() == R.id.tab3){
            setTab(2);
        }else if (view.getId() == R.id.tab4){
            setTab(3);
        }
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
