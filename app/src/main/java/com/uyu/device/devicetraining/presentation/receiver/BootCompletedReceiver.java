package com.uyu.device.devicetraining.presentation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

/**
 * Created by Administrator on 2015/5/31 0031.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootReceiver", "wen boot complete!!!!");

        context.startService(new Intent(context, ScreenService.class));

        Intent newintent = new Intent();
//        newintent.setClass(context, MainActivity.class);
//        newintent.setClass(context, FusionalInspectionActivity.class);
//        newintent.setClass(context, FollowInspectionActivity.class);
//        newintent.setClass(context, EyePositionInspectionActivity.class);
        newintent.setClass(context, WelcomeActivity.class);
//        newintent.setClass(context, EyecardInspectionActivity.class);
//        newintent.setClass(context, AccommodationAmplitudeInspectionActivity.class);
        newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newintent);
    }
}
