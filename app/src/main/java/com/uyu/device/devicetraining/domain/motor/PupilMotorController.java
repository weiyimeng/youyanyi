package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.motor.GapMotor;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlType;

/**
 * Created by windern on 2016/1/18.
 */
public class PupilMotorController extends MotorController<GapMotor>{
    public ControlMessage setPupillaryDistance(int pupillaryDistance){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.SET);
        controlMessage.setValue0(pupillaryDistance);
        return controlMessage;
    }

    public ControlMessage getPupillaryDistance(){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.GET);
        return controlMessage;
    }
}
