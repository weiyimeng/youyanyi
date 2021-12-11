package com.uyu.device.devicetraining.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.domain.bluetooth.ComConfig;
import com.uyu.device.devicetraining.domain.bluetooth.ComFrame;
import com.uyu.device.devicetraining.domain.bluetooth.CommandConvertException;
import com.uyu.device.devicetraining.domain.motor.BaffleMotorController;
import com.uyu.device.devicetraining.domain.motor.ButtonMotorController;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.PupilMotorController;
import com.uyu.device.devicetraining.domain.motor.ScreenMotorController;
import com.uyu.device.devicetraining.domain.motor.TurntableMotorController;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.tv_test)
    TextView tv_test;

    @Bind(R.id.tv_test2)
    TextView tv_test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        //int c = testJni.sub(1,2);
        //Timber.d("c:%s",c);

        ComFrame comFrame = ComFrame.getInstance();
        ControlMessageSet controlMessageSet = getTestControlMessageSet();

        MotorBus.getInstance().sendMessageSet(controlMessageSet);
        MotorBus.getInstance().sendMessageSet(controlMessageSet);

        byte[][] bytesBack = new byte[][]{
            new byte[]{
                (byte)0xa5, 0x22, 0x48, 0x41, 0x0, 0x10, 0xa, 0x0, 0x0, 0x0, 0x0, 0x32, 0x0, 0x0, 0x0,
                0x0, 0x42, 0x0, 0x0, 0x0, 0x0, 0x52, 0x26, 0x2, 0x0, 0x0, 0x62, 0x0, 0x0, 0x0,
                0x0, 0x72, 0x0, 0x0, 0x0, 0x0
            },new byte[]{
                (byte)0xa4, (byte)0xf1, 0x53, (byte)0xf1
                ,(byte)0xa5, 0x22, 0x48, 0x41, 0x0, 0x10
            },new byte[]{
                (byte)0xa5, 0x22, 0x48, 0x41, 0x0, 0x10, 0xa, 0x0, 0x0, 0x0, 0x0, 0x32, 0x0, 0x0, 0x0,
                0x0, 0x42, 0x0, 0x0, 0x0, 0x0, 0x52, 0x26, 0x2, 0x0, 0x0, 0x62, 0x0, 0x0, 0x0,
                0x0, 0x72, 0x0, 0x0, 0x0, 0x0,
                (byte)0xa4, (byte)0xf1, 0x53, (byte)0xf1
                ,(byte)0xa5, 0x22, 0x48, 0x41, 0x0, 0x10
            }
        };

        for(int i=0;i<bytesBack.length;i++){
            byte[] bytes = bytesBack[i];
            comFrame.receiveBytes(bytes);
        }

        //tv_test2.setText(jni.change("333"));
//        Person person = new Person();
//        int a = person.add(5,6);
//        int b = person.sub(11,7);
//        person.setAge(8);
//        int c = person.getAge();

        //传入name="vip"到jni代码模拟拿到加密后的key
//        tv_test.setText(String.valueOf(c));
//        tv_test2.setText(String.valueOf(c));
    }

    public ControlMessageSet getTestControlMessageSet(){
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        ScreenMotorController screenMotCtrl = motorBus.screenMotCtrl;
        ControlMessage screenMessage = screenMotCtrl.reset();
        messageSet.addMessage(screenMessage);

        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.reset();
        messageSet.addMessage(baffleMessage);

        PupilMotorController pupilMotCtrl = motorBus.pupilMotCtrl;
        ControlMessage pupilMessage = pupilMotCtrl.reset();
        messageSet.addMessage(pupilMessage);

        TurntableMotorController rightTurntableMotCtrl = motorBus.rightTurntableMotCtrl;
        ControlMessage rightMessage = rightTurntableMotCtrl.reset();
        messageSet.addMessage(rightMessage);

        TurntableMotorController leftTurntableMotCtrl = motorBus.leftTurntableMotCtrl;
        ControlMessage leftMessage = leftTurntableMotCtrl.reset();
        messageSet.addMessage(leftMessage);

        ButtonMotorController buttonMotCtrl = motorBus.buttonMotCtrl;
        ControlMessage buttonMessage = buttonMotCtrl.reset();
        messageSet.addMessage(buttonMessage);

        return messageSet;
    }
}
