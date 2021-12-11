package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/18.
 */
public class TurntableMotor extends Motor{
    /**
     * 所在位置
     */
    private int location;

    public TurntableMotor(){
        motorType = MotorType.TURNTABLE;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
