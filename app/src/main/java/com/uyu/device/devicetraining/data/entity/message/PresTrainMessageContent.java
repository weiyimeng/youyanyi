package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.trainpres.ApproachTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/5.
 */
public class PresTrainMessageContent extends TrainMessageContent {
    @SerializedName("tp")
    @Expose
    private TrainPres tp;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("optometrist_id")
    @Expose
    private Integer optometristId;
    @SerializedName("reception_id")
    @Expose
    private Integer receptionId;
    @SerializedName("vision_training_feedback_id")
    @Expose
    private Integer visionTrainingFeedbackId;
    @SerializedName("current_repeat_time")
    @Expose
    private Integer currentRepeatTime;

    public TrainPres getTp() {
        return tp;
    }

    public void setTp(TrainPres tp) {
        this.tp = tp;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The optometristId
     */
    public Integer getOptometristId() {
        return optometristId;
    }

    /**
     *
     * @param optometristId
     * The optometrist_id
     */
    public void setOptometristId(Integer optometristId) {
        this.optometristId = optometristId;
    }

    public Integer getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    public Integer getVisionTrainingFeedbackId() {
        return visionTrainingFeedbackId;
    }

    public void setVisionTrainingFeedbackId(Integer visionTrainingFeedbackId) {
        this.visionTrainingFeedbackId = visionTrainingFeedbackId;
    }

    public Integer getCurrentRepeatTime() {
        return currentRepeatTime;
    }

    public void setCurrentRepeatTime(Integer currentRepeatTime) {
        this.currentRepeatTime = currentRepeatTime;
    }

    public static PresTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            PresTrainMessageContent presTrainMessageContent = new PresTrainMessageContent();

            presTrainMessageContent.userId = jsonObject.getInt("user_id");
            presTrainMessageContent.optometristId = jsonObject.getInt("optometrist_id");
            presTrainMessageContent.receptionId = jsonObject.getInt("reception_id");
            presTrainMessageContent.visionTrainingFeedbackId = jsonObject.getInt("vision_training_feedback_id");
            presTrainMessageContent.currentRepeatTime = jsonObject.getInt("current_repeat_time");

            JSONObject tpValue = jsonObject.getJSONObject("tp");

            String train_item_type_value = tpValue.getString("train_item_type");
            EnumTrainItem trainItem = EnumTrainItem.getEnum(train_item_type_value);

            if (trainItem == EnumTrainItem.STEREOSCOPE) {
                StereoscopeTrainPres trainPres = StereoscopeTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.FRACTURED_RULER) {
                FracturedRulerTrainPres trainPres = FracturedRulerTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if(trainItem == EnumTrainItem.REVERSAL){
                ReversalTrainPres trainPres = ReversalTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.RED_GREEN_READ) {
                RedGreenReadTrainPres trainPres = RedGreenReadTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.APPROACH) {
                ApproachTrainPres trainPres = ApproachTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.R_G_VARIABLE_VECTOR) {
                RGVariableVectorTrainPres trainPres = RGVariableVectorTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.R_G_FIXED_VECTOR) {
                RGFixedVectorTrainPres trainPres = RGFixedVectorTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.GLANCE) {
                GlanceTrainPres trainPres = GlanceTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }else if (trainItem == EnumTrainItem.FOLLOW) {
                FollowTrainPres trainPres = FollowTrainPres.convert(tpValue);
                presTrainMessageContent.setTp(trainPres);
            }

            return presTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            JSONObject tpObject = tp.toJson();
            jsonObject.put("tp",tpObject);
            jsonObject.put("user_id",userId);
            jsonObject.put("optometrist_id",optometristId);
            jsonObject.put("reception_id",receptionId);
            jsonObject.put("vision_training_feedback_id",visionTrainingFeedbackId);
            jsonObject.put("current_repeat_time",currentRepeatTime);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
