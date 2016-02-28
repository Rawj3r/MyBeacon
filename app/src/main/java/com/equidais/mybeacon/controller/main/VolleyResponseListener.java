package com.equidais.mybeacon.controller.main;

/**
 * Created by empirestate on 1/29/16.
 */
public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response);
}
