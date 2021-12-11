package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/18.
 */
public class Motor {
    /**
     * 电机类型
     */
    protected MotorType motorType = MotorType.LINE;
    /**
     * 编号
     */
    protected int num = 1;

    public MotorType getMotorType() {
        return motorType;
    }

    public void setMotorType(MotorType motorType) {
        this.motorType = motorType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
