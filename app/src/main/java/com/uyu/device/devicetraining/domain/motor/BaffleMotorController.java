package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlType;

/**
 * Created by windern on 2016/1/18.
 */
public class BaffleMotorController extends LineMotorController{
    public ControlMessage move(int direction,int speed){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.MOVE);
        controlMessage.setValue0(direction);
        controlMessage.setValue1(speed);
        return controlMessage;
    }

    public ControlMessage stop(){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.STOP);
        return controlMessage;
    }
}
