package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.other.Reception;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class ControlExitTrainContent extends ServerEmqttMessageContent {
    @SerializedName("reception")
    @Expose
    private Reception reception;

    /**
     *
     * @return
     * The reception
     */
    public Reception getReception() {
        return reception;
    }

    /**
     *
     * @param reception
     * The reception
     */
    public void setReception(Reception reception) {
        this.reception = reception;
    }

    public static ControlExitTrainContent convert(JSONObject jsonObject) throws Exception {
        try {
            Gson gson = new Gson();
            ControlExitTrainContent controlExitTrainContent = gson.fromJson(jsonObject.toString(),ControlExitTrainContent.class);

            return controlExitTrainContent;
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
