package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/5.
 */
public class TrainMessage {
    @SerializedName("tmt")
    @Expose
    private TrainMessageType tmt= TrainMessageType.START;
    @SerializedName("ct")
    @Expose
    private TrainMessageContent ct;

    public TrainMessageType getTmt() {
        return tmt;
    }

    public void setTmt(TrainMessageType tmt) {
        this.tmt = tmt;
    }

    public TrainMessageContent getCt() {
        return ct;
    }

    public void setCt(TrainMessageContent ct) {
        this.ct = ct;
    }

    public static TrainMessage convert(JSONObject jsonObject) throws JSONException {
        try {
            TrainMessage trainMessage = new TrainMessage();
            String tmtValue = jsonObject.getString("tmt");
            trainMessage.setTmt(TrainMessageType.valueOf(tmtValue));

            if (trainMessage.getTmt() == TrainMessageType.START) {
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                PresTrainMessageContent presTrainMessageContent = PresTrainMessageContent.convert(ctObject);
                trainMessage.setCt(presTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.PASS){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                PassTrainMessageContent passTrainMessageContent = PassTrainMessageContent.convert(ctObject);
                trainMessage.setCt(passTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.FINISH){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                BackTrainMessageContent backTrainMessageContent = BackTrainMessageContent.convert(ctObject);
                trainMessage.setCt(backTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.NORMAL){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                NormalTrainMessageContent normalTrainMessageContent = NormalTrainMessageContent.convert(ctObject);
                trainMessage.setCt(normalTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.EXECUTED){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                ExecutedTrainMessageContent executedTrainMessageContent = ExecutedTrainMessageContent.convert(ctObject);
                trainMessage.setCt(executedTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.EXIT){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                ExitTrainMessageContent exitTrainMessageContent = ExitTrainMessageContent.convert(ctObject);
                trainMessage.setCt(exitTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.CONNECT){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                ConnectTrainMessageContent connectTrainMessageContent = ConnectTrainMessageContent.convert(ctObject);
                trainMessage.setCt(connectTrainMessageContent);
            }else if(trainMessage.getTmt() == TrainMessageType.CONTROLSET){
                JSONObject ctObject = jsonObject.getJSONObject("ct");
                ControlsetTrainMessageContent controlsetTrainMessageContent = ControlsetTrainMessageContent.convert(ctObject);
                trainMessage.setCt(controlsetTrainMessageContent);
            }

            return trainMessage;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tmt",tmt.toString());
            if(ct!=null) {
                JSONObject ctObject = ct.toJson();
                jsonObject.put("ct", ctObject);
            }

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
