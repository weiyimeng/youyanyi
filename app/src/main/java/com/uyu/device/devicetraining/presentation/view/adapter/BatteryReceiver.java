package com.uyu.device.devicetraining.presentation.view.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiOutputPort;

import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 16/9/1.
 */
public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //判断它是否是为电量变化的Broadcast Action
        if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
            //获取当前电量
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            //把它转成百分比
            //tv.setText("电池电量为"+((level*100)/scale)+"%");
            int percent = (level*100)/scale;
            Timber.d("windern:level:%s,scale:%s",level,scale);
            Timber.d("windern:Battery:percent:%s",percent);
            if(percent<30){
                if(MotorBus.getInstance().chargeMotCtrl.getMotor().getChargeStatus()==0) {
                    Timber.d("windern:startcharge");
                    ControlMessageSet messageSet = TrainUseCase.changeChargeStatus(1);
                    MotorBus.getInstance().sendMessageSetDirect(messageSet);
                }
            }else if(percent>=100){
                if(MotorBus.getInstance().chargeMotCtrl.getMotor().getChargeStatus()==1) {
                    Timber.d("windern:stopcharge");
                    ControlMessageSet messageSet = TrainUseCase.changeChargeStatus(0);
                    MotorBus.getInstance().sendMessageSetDirect(messageSet);
                }
            }

        }
    }

}