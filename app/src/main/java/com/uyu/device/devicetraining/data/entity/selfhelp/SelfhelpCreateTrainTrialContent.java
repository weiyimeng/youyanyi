package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class SelfhelpCreateTrainTrialContent extends ServerEmqttMessageContent {
    @SerializedName("reception")
    @Expose
    private ReceptionTrial reception;

    /**
     *
     * @return
     * The reception
     */
    public ReceptionTrial getReception() {
        return reception;
    }

    /**
     *
     * @param reception
     * The reception
     */
    public void setReception(ReceptionTrial reception) {
        this.reception = reception;
    }

    public static SelfhelpCreateTrainTrialContent convert(JSONObject jsonObject) throws Exception {
        try {
            Gson gson = new Gson();
            SelfhelpCreateTrainTrialContent selfhelpCreateTrainTrialContent = gson.fromJson(jsonObject.toString(),SelfhelpCreateTrainTrialContent.class);

            return selfhelpCreateTrainTrialContent;
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
