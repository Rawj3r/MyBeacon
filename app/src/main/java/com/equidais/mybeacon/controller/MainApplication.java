package com.equidais.mybeacon.controller;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.main.AppController;
import com.equidais.mybeacon.controller.main.LruBitmapCache;
import com.equidais.mybeacon.controller.main.TransActivity;
import com.equidais.mybeacon.controller.service.MyService;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by daydreamer on 8/8/2015.
 */
public class MainApplication extends Application  implements BeaconManagerListener {

    private SensoroManager sensoroManager;



    public static final String TAG = AppController.class
            .getSimpleName();
    private static MainApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static final int STATE_INIT = 0;
    public static final int STATE_ENTER_DOOR = 1;
    public static final int STATE_IN_ROOM = 2;
    public static final int STATE_OUT_ROOM_ENTER_DOOR = 3;
    public int mState = STATE_INIT;
    String mBeaconUDID = "";
    public Date mInTime;
    public Date mOutTime;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initSensoroSDK();

        /**
         * Start SDK in Service.
         */
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        startService(intent);
    }

    /**
     * Initial Sensoro SDK.
     */
    private void initSensoroSDK() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.setCloudServiceEnable(true);
        sensoroManager.setBeaconManagerListener(this);
    }

    /**
     * Start Sensoro SDK.
     */
    public void startSensoroSDK() {
        mBeaconUDID = "";
        mState = STATE_INIT;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void stopSensoroSDK() {
        mBeaconUDID = "";
        mState = STATE_INIT;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        try {
            sensoroManager.stopService();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    /**
     * Check whether bluetooth enabled.
     * @return
     */
    public boolean isBluetoothEnabled(){
        return sensoroManager.isBluetoothEnabled();
    }

    @Override
    public void onNewBeacon(Beacon beacon) {
        /**
         * Check whether SDK started in logs.
         */
        Log.e(TAG, beacon.getProximityUUID());
        String beaconUUID = beacon.getProximityUUID();
        if (beaconUUID.equals(mBeaconUDID)){
            if (mState == STATE_INIT){
                mState = STATE_ENTER_DOOR;

            }else if (mState == STATE_IN_ROOM){
                mState = STATE_OUT_ROOM_ENTER_DOOR;
                mOutTime = new Date();

                Log.e(TAG, "out room");
                //send server
                sendServer(beaconUUID);
            }else{
                mState = STATE_ENTER_DOOR;
            }
        }else{
            mState = STATE_ENTER_DOOR;
        }
        mBeaconUDID = beaconUUID;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        Intent intent1 = new Intent(this, TransActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("type", 0);
        startActivity(intent1);
    }

    @Override
    public void onGoneBeacon(Beacon beacon) {
        Log.e(TAG, "gone");
        String beaconUUID = beacon.getProximityUUID();
        if (beaconUUID.equals(mBeaconUDID)){
            if (mState == STATE_ENTER_DOOR){
                mState = STATE_IN_ROOM;
                mInTime = new Date();
                Log.e(TAG, "enter room");
            }else{
                mState = STATE_INIT;
            }
        }else{
            mState = STATE_INIT;
        }
        mBeaconUDID = beaconUUID;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        Intent intent1 = new Intent(this, TransActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("type", 1);
        startActivity(intent1);
    }

    @Override
    public void onUpdateBeacon(ArrayList<Beacon> arrayList) {
        for (int i = 0; i<arrayList.size(); i++){
            Beacon beacon = arrayList.get(i);
            updateView(beacon);
        }
    }

    private void updateView(Beacon beacon) {
        if (beacon == null) {
            return;
        }
        DecimalFormat format = new DecimalFormat("#");
        String distance = format.format(beacon.getAccuracy() * 100);
        Log.e(beacon.getSerialNumber(), "" + distance + " cm");
    }

    private void sendServer(String beaconUDID){
        if (LocalData.getUserID(this) > 0){
            Map<String, Object> map = new HashMap<>();
            map.put("userid", LocalData.getUserID(this));
            map.put("uuid", beaconUDID);
            map.put("deviceudid", GlobalFunc.getDeviceUDID(this));
            map.put("timein", GlobalFunc.getStringParamDate(mInTime));
            map.put("timeout", GlobalFunc.getStringParamDate(mOutTime));
            ApiClient.getApiClient().addPersonGymVisit(map, new Callback<Integer>() {
                @Override
                public void success(Integer integer, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

    }




    public static synchronized MainApplication getInstance() {
        return mInstance;
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





}
