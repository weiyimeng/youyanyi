package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class SelfhelpLockTrainContent extends ServerEmqttMessageContent {
    @SerializedName("reception_id")
    @Expose
    private int receptionId;

    /**
     *
     * @return
     * The receptionId
     */
    public int getReceptionId() {
        return receptionId;
    }

    /**
     *
     * @param receptionId
     * The reception_id
     */
    public void setReceptionId(int receptionId) {
        this.receptionId = receptionId;
    }

    public static SelfhelpLockTrainContent convert(JSONObject jsonObject) throws Exception {
        try {
            Gson gson = new Gson();
            SelfhelpLockTrainContent selfhelpLockTrainContent = gson.fromJson(jsonObject.toString(),SelfhelpLockTrainContent.class);

            return selfhelpLockTrainContent;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws Exception {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);

            JSONObject jsonObject = new JSONObject(json);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
