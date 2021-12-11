package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/3/15.
 */
public class ExecutedTrainMessageContent extends TrainMessageContent {
    //上一个收到的指令
    @SerializedName("pretmt")
    @Expose
    private TrainMessageType pretmt= TrainMessageType.START;

    public TrainMessageType getPretmt() {
        return pretmt;
    }

    public void setPretmt(TrainMessageType pretmt) {
        this.pretmt = pretmt;
    }

    public static ExecutedTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            ExecutedTrainMessageContent executedTrainMessageContent = new ExecutedTrainMessageContent();

            String tmtValue = jsonObject.getString("pretmt");
            executedTrainMessageContent.setPretmt(TrainMessageType.valueOf(tmtValue));

            return executedTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("pretmt",pretmt.toString());

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
