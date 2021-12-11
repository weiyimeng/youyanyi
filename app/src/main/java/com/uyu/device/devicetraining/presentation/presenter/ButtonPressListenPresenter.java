package com.uyu.device.devicetraining.presentation.presenter;

import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.BackMessageSet;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.domain.motor.LocalSocketClient;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.util.MyConfig;
import com.uyu.device.devicetraining.presentation.view.adapter.OnButtonPressListener;

import java.io.IOException;
import java.util.Calendar;

import timber.log.Timber;

/**
 * Created by windern on 2016/2/2.
 */
public class ButtonPressListenPresenter {
    private static int AskLoopTime = 100;
    public int isPress = 0;
    public long pressTime = 0;

    private OnButtonPressListener listener = null;

    public void setListener(OnButtonPressListener listener) {
        this.listener = listener;
    }

    /**
     * 1秒时间过去计时
     */
    private Handler askHandler = new Handler();

    /**
     * 训练记录时间的任务
     */
    private Runnable askRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    ControlMessageSet messageSet = TrainUseCase.getButtonIsPress();

                    LocalSocketClient client = new LocalSocketClient();
                    try {
                        client.connect();
                        client.send(messageSet.toString());
                        String backResult = client.recv();
                        client.close();

                        //Timber.d("send:back:-%s", backResult);
                        if(backResult!=null){
//                        BackMessageSet backMessageSet = BackMessageSet.convert(messageSet);
                            BackMessageSet backMessageSet = BackMessageSet.convert(backResult);
                            if(backMessageSet!=null && backMessageSet.getList().size()>0){
                                BackMessage backMessage = backMessageSet.getList().get(0);
                                int value = backMessage.getValue();
//                            value = isPress;
                                if(value==1){
//                                isPress = 0;
                                    //如果按钮被按下
                                    buttonPress();
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        client.close();
                    }

                    startTimer();
                }
            }).start();
        };
    };

    private void startTimer(){
        askHandler.postDelayed(askRunnable, AskLoopTime);
    }

    private void stopTimer(){
        askHandler.removeCallbacks(askRunnable);
    }

    public void startListen(){
        startTimer();
    }

    public void buttonPress(){
        //不需要去抖
//        Calendar now = Calendar.getInstance();
//        long nowTime = now.getTime().getTime();
//        if(nowTime-pressTime > TrainConfig.ButtonPressShakeTime){
//            pressTime = nowTime;
            if(listener!=null){
                listener.onButtonPress();
            }
//        }
    }
}
