package com.uyu.device.devicetraining.presentation.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.util.Util;

import butterknife.ButterKnife;
import timber.log.Timber;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ButterKnife.bind(this);

        Timber.d("duid:%s", Util.getDuid(FirstActivity.this));

        checkChooseBluetooth();
    }

    public void checkChooseBluetooth(){
        String bltUid = SharePreferenceTool.getSharePreferenceValue(FirstActivity.this,SharePreferenceTool.PREF_CONNECT_BLUTTOOTH_UID);
        //如果没有选择设置蓝牙，选择蓝牙
        if(bltUid.equals("")){
            Intent intent = new Intent(FirstActivity.this,DeviceScanActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(FirstActivity.this,WelcomeActivity.class);
            startActivity(intent);
        }
    }
}
