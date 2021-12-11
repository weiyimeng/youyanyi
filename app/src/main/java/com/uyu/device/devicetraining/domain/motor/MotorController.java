package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.motor.Motor;
import com.uyu.device.devicetraining.data.motor.ControlType;
import com.uyu.device.devicetraining.data.motor.ControlMessage;

/**
 * Created by windern on 2016/1/18.
 */
public class MotorController <M extends Motor>{
    protected MotorBus motorBus;
    protected M motor;

    public void setMotorBus(MotorBus motorBus) {
        this.motorBus = motorBus;
    }

    public void setMotor(M motor) {
        this.motor = motor;
    }

    public M getMotor(){
        return this.motor;
    }

    public ControlMessage reset(){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.RESET);
        return controlMessage;
    }
}
