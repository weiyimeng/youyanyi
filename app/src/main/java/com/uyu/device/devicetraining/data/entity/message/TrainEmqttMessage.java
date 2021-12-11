package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/5.
 */
public class TrainEmqttMessage extends EmqttMessage {
    @SerializedName("stp")
    @Expose
    private String stp;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("rtp")
    @Expose
    private String rtp;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("msg")
    @Expose
    private TrainMessage msg;

    public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRtp() {
        return rtp;
    }

    public void setRtp(String rtp) {
        this.rtp = rtp;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public TrainMessage getMsg() {
        return msg;
    }

    public void setMsg(TrainMessage msg) {
        this.msg = msg;
    }

    public static TrainEmqttMessage convert(String strMsg){
        try {
            JSONObject jsonObject = new JSONObject(strMsg);
            TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();

            trainEmqttMessage.stp = jsonObject.getString("stp");
            trainEmqttMessage.sid = jsonObject.getString("sid");
            trainEmqttMessage.rtp = jsonObject.getString("rtp");
            trainEmqttMessage.rid = jsonObject.getString("rid");
            JSONObject msgObject = jsonObject.getJSONObject("msg");
            trainEmqttMessage.msg = TrainMessage.convert(msgObject);

            return trainEmqttMessage;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stp", stp);
            jsonObject.put("sid", sid);
            jsonObject.put("rtp", rtp);
            jsonObject.put("rid", rid);
            JSONObject msgObject = msg.toJson();
            jsonObject.put("msg",msgObject);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 头部是否一致
     * @param compare
     * @return
     */
    public boolean isHeaderEqual(TrainEmqttMessage compare){
        boolean isEqual = false;
        if(stp.equals(compare.stp)
                && sid.equals(sid)
                && rtp.equals(rtp)
                && rid.equals(rid)){
            isEqual = true;
        }
        return isEqual;
    }
}
