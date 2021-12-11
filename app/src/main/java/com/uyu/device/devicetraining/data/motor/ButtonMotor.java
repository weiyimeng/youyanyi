package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/2/2.
 */
public class ButtonMotor extends Motor {
    /**
     * 是否按下
     */
    private int isPress = 0;

    public ButtonMotor() {
        motorType = MotorType.BUTTON;
    }

    public int getIsPress() {
        return isPress;
    }

    public void setIsPress(int isPress) {
        this.isPress = isPress;
    }
}
