package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/7.
 */
public class PassTrainMessageContent extends TrainMessageContent {
    @SerializedName("pass_level")
    @Expose
    private Integer passLevel;

    public Integer getPassLevel() {
        return passLevel;
    }

    public void setPassLevel(Integer passLevel) {
        this.passLevel = passLevel;
    }

    public static PassTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            PassTrainMessageContent passTrainMessageContent = new PassTrainMessageContent();
            passTrainMessageContent.passLevel = jsonObject.getInt("pass_level");
            return passTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("pass_level",passLevel);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
