package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/18.
 */
public class LineMotor extends Motor{
    /**
     * 位置
     */
    private int location = 0;

    public LineMotor(){
        motorType = MotorType.LINE;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
