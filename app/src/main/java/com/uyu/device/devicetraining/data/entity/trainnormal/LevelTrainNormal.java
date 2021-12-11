package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class LevelTrainNormal extends TrainNormal {
    @SerializedName("now_level")
    @Expose
    private Integer nowLevel=1;
    @SerializedName("fail_time")
    @Expose
    private Integer failTime=0;

    /**
     *
     * @return
     * The nowLevel
     */
    public Integer getNowLevel() {
        return nowLevel;
    }

    /**
     *
     * @param nowLevel
     * The now_level
     */
    public void setNowLevel(Integer nowLevel) {
        this.nowLevel = nowLevel;
    }

    /**
     *
     * @return
     * The failTime
     */
    public Integer getFailTime() {
        return failTime;
    }

    /**
     *
     * @param failTime
     * The fail_time
     */
    public void setFailTime(Integer failTime) {
        this.failTime = failTime;
    }

    public LevelTrainNormal(){
        this.setTrainItemType(EnumTrainItem.STEREOSCOPE);
    }

    public static LevelTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            LevelTrainNormal trainNormal = new LevelTrainNormal();
            trainNormal.convertParent(jsonObject);

            trainNormal.setNowLevel(jsonObject.getInt("now_level"));
            trainNormal.setFailTime(jsonObject.getInt("fail_time"));

            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("now_level", nowLevel);
            jsonObject.put("fail_time", failTime);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);
            setNowLevel(jsonObject.getInt("now_level"));
            setFailTime(jsonObject.getInt("fail_time"));
        }catch (JSONException e){
            throw e;
        }
    }
}
