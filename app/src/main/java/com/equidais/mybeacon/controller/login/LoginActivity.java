package com.equidais.mybeacon.controller.login;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.common.JSONParser;
import com.equidais.mybeacon.controller.main.FeedbackActivity;
import com.equidais.mybeacon.controller.main.MainActivity;
import com.equidais.mybeacon.model.LoginResult;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_ENABLE_BT = 3;
    EditText mEditEmail;
    EditText mEditPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditEmail = (EditText)findViewById(R.id.edit_email);
        mEditPassword = (EditText)findViewById(R.id.edit_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        if (LocalData.getUserID(this) > 0){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        }
        beatconTest();
    }

    private void beatconTest(){
        SensoroManager sensoroManager = SensoroManager.getInstance(this);
        /**
         * Check whether the Bluetooth is on
         **/
        if (sensoroManager.isBluetoothEnabled()) {
            /**
             * Enable cloud service (upload sensor data, including battery status, UMM, etc.). Without setup, it keeps in closed status as default.
             **/
            sensoroManager.setCloudServiceEnable(true);
            /**
             * Enable SDK service
             **/
            try {
                sensoroManager.startService();
            } catch (Exception e) {
                e.printStackTrace(); // Fetch abnormal info
            }
            sensoroManager.addBroadcastKey("0117C585C080");
            BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {

                @Override
                public void onUpdateBeacon(ArrayList<Beacon> beacons) {
                    // Refresh sensor info
                    for (int i = 0; i<beacons.size(); i++){
                        Beacon beacon = beacons.get(i);
                        updateView(beacon);
                    }
                }

                @Override
                public void onNewBeacon(Beacon beacon) {
                    // New sensor found


                    if (beacon.getSerialNumber().equals("0117C585C080")){
                        Toast.makeText(LoginActivity.this, "find beacon", Toast.LENGTH_SHORT).show();
                        // Yunzi with SN "0117C5456A36" enters the range
                    }
                }

                @Override
                public void onGoneBeacon(Beacon beacon) {
                    // A sensor disappears from the range
                }
            };
            sensoroManager.setBeaconManagerListener(beaconManagerListener);
        }else{
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
        }
    }

    private void updateView(Beacon beacon) {
        if (beacon == null) {
            return;
        }
        DecimalFormat format = new DecimalFormat("#");
        String distance = format.format(beacon.getAccuracy() * 100);
//        ((TextView)findViewById(R.id.txt_test)).setText("" + distance + " cm");
    }

    ProgressDialog mProgressDlg;
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login){
            final String email = mEditEmail.getText().toString();
            final String password = mEditPassword.getText().toString();

            //Toast.makeText(this, email + "/n " +  password, Toast.LENGTH_LONG).show();
            if (email.equals("")){
                GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                        getResources().getString(R.string.alert_input_email));
                return;
            }
            if (password.equals("")){
                GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                        getResources().getString(R.string.alert_input_password));
                return;
            }

            class  Login extends AsyncTask<String, String, JSONObject>{

                JSONParser jsonParser = new JSONParser();
                private ProgressDialog progressDialog;
                private static final String LOGIN_URL = "http://masscash.empirestate.co.za/BeaconAppTest/phpStoredProcedure/API/login.php";

                private static final String TAG_SUCCESS = "success";
                private static final String TAG_MESSAGE = "message";

                String id = android.provider.Settings.System.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Login in...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }

                @Override
                protected JSONObject doInBackground(String... params) {

                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("deviceUDID", id);
                        hashMap.put("username", email);
                        hashMap.put("password_", password);

                        Log.d("request", "starting");

                        JSONObject jsonObject = jsonParser.makeHttpRequest(LOGIN_URL, "POST", hashMap);

                        if (jsonObject != null){
                            Log.d("JSON result", jsonObject.toString());

                            return jsonObject;
                        }
                    }catch (Exception e){

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {

                    int success = 0;
                    String message = "";

                    if (progressDialog != null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    if (jsonObject != null){
                        Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();

                        try {
                            success = jsonObject.getInt(TAG_SUCCESS);
                            message = jsonObject.getString(TAG_MESSAGE);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    if (success == 1){
                        Log.d("success", message);

                        SharedPreferences preferences = getSharedPreferences("myBeaconUsername", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("username", email);
                        editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        i.putExtra("username", email);
                        startActivity(i);
                        finish();
                    }else {
                        Log.d("Failure", message);
                    }

                }
            }


           /* mProgressDlg = GlobalFunc.showProgressDialog(this);


            class getData extends AsyncTask<String, String, String>{

                HttpURLConnection connection;
                @Override
                protected String doInBackground(String... params) {
                    StringBuilder stringBuilder = new StringBuilder();

                    try {
                        URL url = new URL("http://masscash.empirestate.co.za/BeaconAppTest/phpStoredProcedure/API/");
                        connection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;

                        while((line = bufferedReader.readLine()) != null){
                            stringBuilder.append(line);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        connection.disconnect();
                    }
                    return stringBuilder.toString();
                }

                @Override
                protected void onPostExecute(String s) {

                }
            }
*/
            /*ApiClient.getApiClient().login(hashMap, new Callback<LoginResult>() {

                @Override
                public void success(LoginResult loginResult, Response response) {
                    mProgressDlg.dismiss();

                    if (loginResult.IsSuccess) {
                        Toast.makeText(LoginActivity.this, "123", Toast.LENGTH_LONG).show();

                        LocalData.setUsernameAndPassword(LoginActivity.this, email, password, loginResult.UserID);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (loginResult.Message != null && !loginResult.Message.equals("")) {
                        Toast.makeText(LoginActivity.this, "123456", Toast.LENGTH_LONG).show();

                        GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                                loginResult.Message);
                        System.out.println();
                    } else {
                        Toast.makeText(LoginActivity.this, "123456789", Toast.LENGTH_LONG).show();

                        GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                                getResources().getString(R.string.alert_connect_failed));

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LoginActivity.this, "9", Toast.LENGTH_LONG).show();
                    mProgressDlg.dismiss();
                    GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                            getResources().getString(R.string.alert_connect_failed));
                }
            });*/
            new Login().execute();

        }


    }
}
