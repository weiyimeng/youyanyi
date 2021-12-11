package com.uyu.device.devicetraining.data.entity.message;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by jwc on 2016/8/15.
 */
public class VersionEmqttMessage{

    @SerializedName("msg_type")
    @Expose
    private String msgType;

    @SerializedName("msg_content")
    @Expose
    private VersionEmqttMessageContent msgContent;

    public static VersionEmqttMessage convert(JSONObject jsonObject){
        try {
            VersionEmqttMessage versionEmqttMessage = new VersionEmqttMessage();
            versionEmqttMessage.msgType = jsonObject.getString("msg_type");
            JSONObject msgObject = jsonObject.getJSONObject("msg_content");
            versionEmqttMessage.msgContent = VersionEmqttMessageContent.convert(msgObject);

            return versionEmqttMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMsgType() {
        return msgType;
    }

    public VersionEmqttMessageContent getMsgContent() {
        return msgContent;
    }
}
