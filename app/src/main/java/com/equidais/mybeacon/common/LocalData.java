package com.equidais.mybeacon.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daydreamer on 8/12/2015.
 */
public class LocalData {
    public static String getUserName(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(GlobalConst.PREF_ATTR_USERNAME, "");
    }

    public static int getUserID(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(GlobalConst.PREF_ATTR_USERID, -1);
    }

    public static String getPassword(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(GlobalConst.PREF_ATTR_PASSWORD, "");
    }

    public static void setUsernameAndPassword(Context context, String userName, String password, int userID){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(GlobalConst.PREF_ATTR_USERNAME, userName);
        editor.putString(GlobalConst.PREF_ATTR_PASSWORD, password);
        editor.putInt(GlobalConst.PREF_ATTR_USERID, userID);
        editor.commit();
    }


}
