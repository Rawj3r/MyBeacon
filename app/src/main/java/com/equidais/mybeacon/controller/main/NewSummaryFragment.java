package com.equidais.mybeacon.controller.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.equidais.mybeacon.R;
import com.equidais.mybeacon.controller.MainApplication;

import java.util.HashMap;
import java.util.Map;


public class NewSummaryFragment extends Fragment {
    private static final String LOG = "test data";
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    public static NewSummaryFragment newInstance(int pos) {
        NewSummaryFragment fragment = new NewSummaryFragment();
        Bundle args = new Bundle();
       args.putInt("ss",pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sendData("3");

        return inflater.inflate(R.layout.fragment_new_summary, container, false);
    }



    public void sendData(final String username) {

        final StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.MAIN_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG, "`response fro Simba  Response: " + response.toString());
                        String res = response.toString();
                        if (!res.isEmpty()) {

                            Log.d(LOG, "response fro the server " + res);

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
