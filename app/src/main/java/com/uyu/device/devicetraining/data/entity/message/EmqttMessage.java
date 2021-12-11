package com.uyu.device.devicetraining.data.entity.message;

import android.util.Log;

import com.uyu.device.devicetraining.data.entity.selfhelp.ServerEmqttMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class EmqttMessage {
    public static EmqttMessage convert(String strMsg){
        try {
            JSONObject jsonObject = new JSONObject(strMsg);
            if(strMsg.contains("PUSH_LATEST_VERSIONS")){
                VersionEmqtt versionEmqtt = VersionEmqtt.convert(strMsg);
                return versionEmqtt;
            }else if(jsonObject.has("mt")){
                ServerEmqttMessage serverEmqttMessage = ServerEmqttMessage.convert(strMsg);
                return serverEmqttMessage;
            }else{
                TrainEmqttMessage trainEmqttMessage = TrainEmqttMessage.convert(strMsg);
                return trainEmqttMessage;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson(){
        return "";
    }
}
