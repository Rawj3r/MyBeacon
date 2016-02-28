package com.equidais.mybeacon.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.common.BaseFragment;


public class TransActivity extends BaseActivity {


    int mType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        getSupportActionBar().hide();

        mType = getIntent().getIntExtra("type", 0);
        if (mType == 0){
            Toast toast = Toast.makeText(this, R.string.alert_welcome, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Toast toast = Toast.makeText(this, R.string.alert_thank, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
