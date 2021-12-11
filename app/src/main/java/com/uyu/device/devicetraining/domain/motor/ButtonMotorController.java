package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.motor.ButtonMotor;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlType;
import com.uyu.device.devicetraining.data.motor.GapMotor;

/**
 * Created by windern on 2016/1/18.
 */
public class ButtonMotorController extends MotorController<ButtonMotor>{
    public ControlMessage getButtonIsPress(){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.GET);
        return controlMessage;
    }
}
