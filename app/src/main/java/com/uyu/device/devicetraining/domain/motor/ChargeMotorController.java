package com.uyu.device.devicetraining.domain.motor;

import com.uyu.device.devicetraining.data.motor.ButtonMotor;
import com.uyu.device.devicetraining.data.motor.ChargeMotor;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlType;

/**
 * Created by windern on 2016/1/18.
 */
public class ChargeMotorController extends MotorController<ChargeMotor>{
    public ControlMessage changeChargeStatus(int chargeStatus){
        ControlMessage controlMessage = new ControlMessage();
        controlMessage.setMotorNum(motor.getNum());
        controlMessage.setControlType(ControlType.CHARGE);
        controlMessage.setValue0(chargeStatus);
        return controlMessage;
    }
}
