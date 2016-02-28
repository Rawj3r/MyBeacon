package com.equidais.mybeacon.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.equidais.mybeacon.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daydreamer on 8/12/2015.
 */
public class GlobalFunc {

    public static void showAlertDialog(Context context, String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        if (!content.equals("")){
            builder.setMessage(content);
        }
        builder.show();
    }

    public static ProgressDialog showProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.waiting));
        progressDialog.show();
        return progressDialog;
    }

    public static String getDeviceUDID(Context context){
        String id = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return id;
    }


    public static int getPushState(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(GlobalConst.PREF_ATTR_PUSH_STATE, 0);
    }

    public static void savePushState(Context context, int state){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(GlobalConst.PREF_ATTR_PUSH_STATE, state);
        editor.commit();
    }

    public static String getPushId(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(GlobalConst.PREF_ATTR_PUSH_REGID, "");
    }

    public static void savePushId(Context context, String pushId){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(GlobalConst.PREF_ATTR_PUSH_REGID, pushId);
        editor.commit();
    }

    public static String getDateFormatStringFromDuration(long duration){
        String isMinus = "";
        if (duration < 0){
            isMinus = "-";
            duration = -duration;
        }
        int hours = (int)(duration / 3600);
        int minutes = (int)((duration % 3600) / 60);
        int seconds = (int)(duration % 60);
        if (hours == 0){
            return isMinus + String.format("%02d:%02d", minutes, seconds);
        }else {
            return isMinus + String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public static String getDateString(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat showFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Date date = format.parse(strDate);
            return showFormat.format(date);
        }catch (Exception e){
            return "";
        }
    }

    public static boolean isSameWeek(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try{
            Date date = format.parse(strDate);
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();
            calendar.setTime(date);
            calToday.setTime(nowTime);
            int year1 = calendar.get(Calendar.YEAR);
            int week1 = calendar.get(Calendar.WEEK_OF_YEAR);
            int year2 = calToday.get(Calendar.YEAR);
            int week2 = calToday.get(Calendar.WEEK_OF_YEAR);
            if (year1 == year2 && week1 == week2){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isSameMonth(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try{
            Date date = format.parse(strDate);
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();
            calendar.setTime(date);
            calToday.setTime(nowTime);
            int year1 = calendar.get(Calendar.YEAR);
            int week1 = calendar.get(Calendar.MONTH);
            int year2 = calToday.get(Calendar.YEAR);
            int week2 = calToday.get(Calendar.MONTH);
            if (year1 == year2 && week1 == week2){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public static String getDurationFrom2Dates(String inTime, String outTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try{
            Date date1 = format.parse(inTime);
            Date date2 = format.parse(outTime);
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(date1);
            calendar2.setTime(date2);
            long milliSeconds = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis())/1000;
            return getDateFormatStringFromDuration(milliSeconds);
        }catch (Exception e){
            return "";
        }
    }

    public static String getDurationFromInTime(Date inTime){

        try{

            Date today = new Date();
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(inTime);
            calendar2.setTime(today);
            long milliSeconds = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis())/1000;
            return getDateFormatStringFromDuration(milliSeconds);
        }catch (Exception e){
            return "";
        }
    }


    public static String getStringParamDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try{
            return format.format(date);
        }catch (Exception e){
            return "";
        }
    }
}
