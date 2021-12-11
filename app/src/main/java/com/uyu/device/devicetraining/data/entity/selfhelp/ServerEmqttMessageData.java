package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class ServerEmqttMessageData {
    @SerializedName("msg_type")
    @Expose
    private EnumServerMessageType msgType;

    @SerializedName("msg_content")
    @Expose
    private ServerEmqttMessageContent msgContent;

    /**
     *
     * @return
     * The msgType
     */
    public EnumServerMessageType getMsgType() {
        return msgType;
    }

    /**
     *
     * @param msgType
     * The msg_type
     */
    public void setMsgType(EnumServerMessageType msgType) {
        this.msgType = msgType;
    }

    /**
     *
     * @return
     * The msgContent
     */
    public ServerEmqttMessageContent getMsgContent() {
        return msgContent;
    }

    /**
     *
     * @param msgContent
     * The msg_content
     */
    public void setMsgContent(ServerEmqttMessageContent msgContent) {
        this.msgContent = msgContent;
    }

    public static ServerEmqttMessageData convert(JSONObject jsonObject) throws Exception {
        try {
            ServerEmqttMessageData serverMessage = new ServerEmqttMessageData();
            String msgTypeValue = jsonObject.getString("msg_type");
            serverMessage.setMsgType(EnumServerMessageType.getValueOf(msgTypeValue));

            if (serverMessage.getMsgType() == EnumServerMessageType.SELFHELP_CREATE_TRAIN) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                SelfhelpCreateTrainContent selfhelpCreateTrainContent = SelfhelpCreateTrainContent.convert(msgContentObject);
                serverMessage.setMsgContent(selfhelpCreateTrainContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.SELFHELP_FINISH_TRAIN) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                SelfhelpFinishTrainContent selfhelpFinishTrainContent = SelfhelpFinishTrainContent.convert(msgContentObject);
                serverMessage.setMsgContent(selfhelpFinishTrainContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.SELFHELP_LOCK_TRAIN) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                SelfhelpLockTrainContent selfhelpLockTrainContent = SelfhelpLockTrainContent.convert(msgContentObject);
                serverMessage.setMsgContent(selfhelpLockTrainContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.SELFHELP_UNLOCK_TRAIN) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                SelfhelpUnlockTrainContent selfhelpUnlockTrainContent = SelfhelpUnlockTrainContent.convert(msgContentObject);
                serverMessage.setMsgContent(selfhelpUnlockTrainContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.CONTROL_EXIT_TRAIN) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                ControlExitTrainContent controlExitTrainContent = ControlExitTrainContent.convert(msgContentObject);
                serverMessage.setMsgContent(controlExitTrainContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.SELFHELP_CREATE_TRAIN_TRIAL) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                SelfhelpCreateTrainTrialContent selfhelpCreateTrainTrialContent = SelfhelpCreateTrainTrialContent.convert(msgContentObject);
                serverMessage.setMsgContent(selfhelpCreateTrainTrialContent);
            }else if (serverMessage.getMsgType() == EnumServerMessageType.PUSH_TRAIN_CONTENT) {
                JSONObject msgContentObject = jsonObject.getJSONObject("msg_content");
                PushTrainContentContent pushTrainContentContent = PushTrainContentContent.convert(msgContentObject);
                serverMessage.setMsgContent(pushTrainContentContent);
            }

            return serverMessage;
        }catch (Exception e){
            throw e;
        }
    }

    public JSONObject toJson() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg_type",msgType.toString());
            if(msgContent!=null) {
                JSONObject msgContentObject = msgContent.toJson();
                jsonObject.put("msg_content", msgContentObject);
            }

            return jsonObject;
        }catch (Exception e){
            throw e;
        }
    }
}
