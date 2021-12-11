package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;

import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class ServerEmqttMessage extends EmqttMessage {
    @SerializedName("mt")
    @Expose
    private String mt;

    @SerializedName("msg")
    @Expose
    private ServerEmqttMessageData msg;

    /**
     *
     * @return
     * The mt
     */
    public String getMt() {
        return mt;
    }

    /**
     *
     * @param mt
     * The mt
     */
    public void setMt(String mt) {
        this.mt = mt;
    }

    /**
     *
     * @return
     * The msg
     */
    public ServerEmqttMessageData getMsg() {
        return msg;
    }

    /**
     *
     * @param msg
     * The msg
     */
    public void setMsg(ServerEmqttMessageData msg) {
        this.msg = msg;
    }

    public static ServerEmqttMessage convert(String strMsg){
        try {
            ServerEmqttMessage serverEmqttMessage = new ServerEmqttMessage();
            JSONObject jsonObject = new JSONObject(strMsg);

            serverEmqttMessage.mt = jsonObject.getString("mt");
            JSONObject msgObject = jsonObject.getJSONObject("msg");
            serverEmqttMessage.msg = ServerEmqttMessageData.convert(msgObject);

            return serverEmqttMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mt",mt.toString());
            JSONObject msgObject = msg.toJson();
            jsonObject.put("msg", msgObject);

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
