package com.uyu.device.devicetraining.data.motor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/1/20.
 */
public class BackMessage {
    private int seqNum = 1;
    private FlagType flag = FlagType.SUCCESS;
    private int value = 0;

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public FlagType getFlag() {
        return flag;
    }

    public void setFlag(FlagType flag) {
        this.flag = flag;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String value = "";
        try {
            JSONObject jsonObject = toJson();
            value = jsonObject.toString();
        } catch (JSONException e) {
            value = "";
            e.printStackTrace();
        }

        return value;
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("seq_num",seqNum);
            jsonObject.put("flag", flag.getValue());
            jsonObject.put("value", value);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    public static BackMessage convert(String msgString){
        BackMessage backMessage = null;
        try {
            backMessage = new BackMessage();
            JSONObject jsonObject = new JSONObject(msgString);
            backMessage = convert(jsonObject);
        } catch (JSONException e) {
            backMessage = null;
            e.printStackTrace();
        }
        return backMessage;
    }

    public static BackMessage convert(JSONObject jsonObject){
        BackMessage backMessage = null;
        try {
            backMessage = new BackMessage();
            backMessage.setSeqNum(jsonObject.getInt("seq_num"));
            int flagTypeValue = jsonObject.getInt("flag");
            backMessage.setFlag(FlagType.values()[flagTypeValue]);
            backMessage.setValue(jsonObject.getInt("value"));
        } catch (JSONException e) {
            backMessage = null;
            e.printStackTrace();
        }
        return backMessage;
    }

    public static BackMessage convert(ControlMessage controlMessage){
        BackMessage backMessage = null;
        backMessage = new BackMessage();
        backMessage.setSeqNum(controlMessage.getSeqNum());
        backMessage.setFlag(FlagType.SUCCESS);
        backMessage.setValue(controlMessage.getValue0());
        return backMessage;
    }

    /**
     * 是否是按钮按下消息
     * @return
     */
    public boolean isButtonPressMessage(){
        return seqNum==0;
    }

    /**
     * 是否改变充电状态的消息
     * @return
     */
    public boolean isChangeChargeMessage(){
        return seqNum==-1;
    }
}
