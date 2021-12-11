package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/7.
 */
public class ConnectTrainMessageContent extends TrainMessageContent {
    @SerializedName("reception_id")
    @Expose
    private Integer receptionId;

    /**
     *
     * @return
     * The receptionId
     */
    public Integer getReceptionId() {
        return receptionId;
    }

    /**
     *
     * @param receptionId
     * The reception_id
     */
    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    public static ConnectTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            ConnectTrainMessageContent passTrainMessageContent = new ConnectTrainMessageContent();
            passTrainMessageContent.receptionId = jsonObject.getInt("reception_id");
            return passTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("reception_id",receptionId);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
