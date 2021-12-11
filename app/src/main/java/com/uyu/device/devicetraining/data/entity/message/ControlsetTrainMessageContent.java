package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/7.
 */
public class ControlsetTrainMessageContent extends TrainMessageContent {
    @SerializedName("motor_num")
    @Expose
    private int motorNum;

    @SerializedName("value")
    @Expose
    private int value;

    public int getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(int motorNum) {
        this.motorNum = motorNum;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static ControlsetTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            ControlsetTrainMessageContent controlsetTrainMessageContent = new ControlsetTrainMessageContent();
            controlsetTrainMessageContent.motorNum = jsonObject.getInt("motor_num");
            controlsetTrainMessageContent.value = jsonObject.getInt("value");
            return controlsetTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("motor_num",motorNum);
            jsonObject.put("value",value);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
