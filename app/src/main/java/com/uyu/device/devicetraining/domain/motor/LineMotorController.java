package com.uyu.device.devicetraining.domain.motor;

import android.os.Handler;

import com.uyu.device.devicetraining.data.motor.LineMotor;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlType;

/**
 * Created by windern on 2016/1/18.
 */
public class LineMotorController extends MotorController<LineMotor>{
    public ControlMessage setLocation(int location){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.SET);
        controlMessage.setValue0(location);
        return controlMessage;
    }

    public ControlMessage getLocation(){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.GET);
        return controlMessage;
    }
}
