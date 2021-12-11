package com.uyu.device.devicetraining.data.motor;

/**
 * 充电电机
 * Created by windern on 2016/2/2.
 */
public class ChargeMotor extends Motor {
    /**
     * 是否充电开启，0关闭状态1开启状态
     * 默认1开启，设备启动就开启
     */
    private int chargeStatus = 1;

    public ChargeMotor() {
        motorType = MotorType.CHARGE;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }
}
