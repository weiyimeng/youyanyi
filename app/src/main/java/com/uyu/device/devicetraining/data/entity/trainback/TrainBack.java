package com.uyu.device.devicetraining.data.entity.trainback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/8.
 */
public class TrainBack extends BaseModel {
    @SerializedName("local_id")
    @Expose
    private int localId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("optometrist_id")
    @Expose
    private Integer optometristId;
    @SerializedName("vision_training_feedback_id")
    @Expose
    private Integer visionTrainingFeedbackId;
    @SerializedName("item_train_pres_id")
    @Expose
    private Integer itemTrainPresId;
    @SerializedName("current_repeat_time")
    @Expose
    private Integer currentRepeatTime;
    @SerializedName("training_start_date")
    @Expose
    private Integer trainingStartDate;
    @SerializedName("training_end_date")
    @Expose
    private Integer trainingEndDate;
    @SerializedName("total_time")
    @Expose
    private Integer totalTime;
    @SerializedName("train_item_type")
    @Expose
    private EnumTrainItem trainItemType;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
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

    /**
     *
     * @return
     * The itemTrainPresId
     */
    public Integer getItemTrainPresId() {
        return itemTrainPresId;
    }

    /**
     *
     * @param itemTrainPresId
     * The item_train_pres_id
     */
    public void setItemTrainPresId(Integer itemTrainPresId) {
        this.itemTrainPresId = itemTrainPresId;
    }

    /**
     *
     * @return
     * The currentRepeatTime
     */
    public Integer getCurrentRepeatTime() {
        return currentRepeatTime;
    }

    /**
     *
     * @param currentRepeatTime
     * The current_repeat_time
     */
    public void setCurrentRepeatTime(Integer currentRepeatTime) {
        this.currentRepeatTime = currentRepeatTime;
    }

    /**
     *
     * @return
     * The trainingStartDate
     */
    public Integer getTrainingStartDate() {
        return trainingStartDate;
    }

    /**
     *
     * @param trainingStartDate
     * The training_start_date
     */
    public void setTrainingStartDate(Integer trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    /**
     *
     * @return
     * The trainingEndDate
     */
    public Integer getTrainingEndDate() {
        return trainingEndDate;
    }

    /**
     *
     * @param trainingEndDate
     * The training_end_date
     */
    public void setTrainingEndDate(Integer trainingEndDate) {
        this.trainingEndDate = trainingEndDate;
    }

    /**
     *
     * @return
     * The totalTime
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     *
     * @param totalTime
     * The total_time
     */
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    /**
     *
     * @return
     * The trainItemType
     */
    public EnumTrainItem getTrainItemType() {
        return trainItemType;
    }

    /**
     *
     * @param trainItemType
     * The train_item_type
     */
    public void setTrainItemType(EnumTrainItem trainItemType) {
        this.trainItemType = trainItemType;
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            localId = jsonObject.getInt("local_id");
            id = jsonObject.getInt("id");
            userId = jsonObject.getInt("user_id");
            optometristId = jsonObject.getInt("optometrist_id");
            visionTrainingFeedbackId = jsonObject.getInt("vision_training_feedback_id");
            itemTrainPresId = jsonObject.getInt("item_train_pres_id");
            currentRepeatTime = jsonObject.getInt("current_repeat_time");
            trainingStartDate = jsonObject.getInt("training_start_date");
            trainingEndDate = jsonObject.getInt("training_end_date");
            totalTime = jsonObject.getInt("total_time");

            String train_item_type_value = jsonObject.getString("train_item_type");
            trainItemType = EnumTrainItem.getEnum(train_item_type_value);
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("local_id",localId);
            jsonObject.put("id",id);
            jsonObject.put("user_id",userId);
            jsonObject.put("optometrist_id",optometristId);
            jsonObject.put("vision_training_feedback_id",visionTrainingFeedbackId);
            jsonObject.put("item_train_pres_id",itemTrainPresId);
            jsonObject.put("current_repeat_time",currentRepeatTime);
            jsonObject.put("training_start_date",trainingStartDate);
            jsonObject.put("training_end_date",trainingEndDate);
            jsonObject.put("total_time",totalTime);
            jsonObject.put("train_item_type",trainItemType.toString());

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
