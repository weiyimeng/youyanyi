package com.uyu.device.devicetraining.data.entity.trainback.trial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.config.RGVariableVectorConfig;
import com.uyu.device.devicetraining.data.entity.trainback.RGVariableVectorTrainBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/6/2.
 */
public class TrialRGVariableVectorTrainBack extends RGVariableVectorTrainBack {
    @SerializedName("score")
    @Expose
    private Integer score;

    /**
     *
     * @return
     * The score
     */
    public Integer getScore() {
        return score;
    }

    /**
     *
     * @param score
     * The score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            setScore(jsonObject.getInt("score"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static TrialRGVariableVectorTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            TrialRGVariableVectorTrainBack trainBack = new TrialRGVariableVectorTrainBack();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("score", score);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    /**
     * 计算得分
     */
    public void computeScore(){
        score = (int)((criticalLocation * 100)/(RGVariableVectorConfig.Max_Distance * 2));
    }
}
