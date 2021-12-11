package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/7.
 */
public class ExitTrainMessageContent extends TrainMessageContent {
    @SerializedName("reception_id")
    @Expose
    private Integer receptionId;
    @SerializedName("vision_training_feedback_id")
    @Expose
    private Integer visionTrainingFeedbackId;

    /**
     *
     * @return
     * The receptionId
     */
    public Integer getReceptionId() {
        return receptionId;
    }

    /**
     *
     * @param receptionId
     * The reception_id
     */
    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    /**
     *
     * @return
     * The visionTrainingFeedbackId
     */
    public Integer getVisionTrainingFeedbackId() {
        return visionTrainingFeedbackId;
    }

    /**
     *
     * @param visionTrainingFeedbackId
     * The vision_training_feedback_id
     */
    public void setVisionTrainingFeedbackId(Integer visionTrainingFeedbackId) {
        this.visionTrainingFeedbackId = visionTrainingFeedbackId;
    }

    public static ExitTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            ExitTrainMessageContent passTrainMessageContent = new ExitTrainMessageContent();
            passTrainMessageContent.receptionId = jsonObject.getInt("reception_id");
            passTrainMessageContent.visionTrainingFeedbackId = jsonObject.getInt("vision_training_feedback_id");
            return passTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("reception_id",receptionId);
            jsonObject.put("vision_training_feedback_id",visionTrainingFeedbackId);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
