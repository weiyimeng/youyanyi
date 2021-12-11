package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by jwc on 2016/8/17.
 */
public class VersionEmqtt extends EmqttMessage {

    @SerializedName("msg")
    @Expose
    private VersionEmqttMessage msg;

    public static VersionEmqtt convert(String strMsg){
        try {
            VersionEmqtt versionEmqtt = new VersionEmqtt();
            JSONObject jsonObject = new JSONObject(strMsg);
            JSONObject msgObject = jsonObject.getJSONObject("msg");
            versionEmqtt.msg = VersionEmqttMessage.convert(msgObject);

            return versionEmqtt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public VersionEmqttMessage getMsg() {
        return msg;
    }
}
