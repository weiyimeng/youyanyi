package com.uyu.device.devicetraining.data.entity.trainback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.InheritedColumn;
import com.raizlabs.android.dbflow.annotation.InheritedPrimaryKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.uyu.device.devicetraining.data.db.AppDatabase;
import com.uyu.device.devicetraining.data.entity.config.trial.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;
import com.uyu.device.devicetraining.data.entity.type.EnumPresAdjust;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.entity.util.ScoreManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2015/12/8.
 */
@Table(database = AppDatabase.class,
        inheritedColumns = {@InheritedColumn(column = @Column, fieldName = "id")
                , @InheritedColumn(column = @Column, fieldName = "userId")
                , @InheritedColumn(column = @Column, fieldName = "optometristId")
                , @InheritedColumn(column = @Column, fieldName = "visionTrainingFeedbackId")
                , @InheritedColumn(column = @Column, fieldName = "itemTrainPresId")
                , @InheritedColumn(column = @Column, fieldName = "currentRepeatTime")
                , @InheritedColumn(column = @Column, fieldName = "trainingStartDate")
                , @InheritedColumn(column = @Column, fieldName = "trainingEndDate")
                , @InheritedColumn(column = @Column, fieldName = "totalTime")
                , @InheritedColumn(column = @Column, fieldName = "train_item_type")
        },
        inheritedPrimaryKeys = {@InheritedPrimaryKey(column = @Column,
                primaryKey = @PrimaryKey(autoincrement = true),
                fieldName = "localId")})
public class ReversalTrainBack extends TrainBack {
    @SerializedName("prescription_adjust_status")
    @Expose
    @Column
    protected EnumPresAdjust prescriptionAdjustStatus;
    @SerializedName("eye_type")
    @Expose
    @Column
    protected EnumEyeType eyeType;
    @SerializedName("score")
    @Expose
    @Column
    protected Integer score = 100;
    @SerializedName("l_positive_degree_level")
    @Expose
    @Column
    protected Integer lPositiveDegreeLevel;
    @SerializedName("l_negative_degree_level")
    @Expose
    @Column
    protected Integer lNegativeDegreeLevel;
    @SerializedName("r_positive_degree_level")
    @Expose
    @Column
    protected Integer rPositiveDegreeLevel;
    @SerializedName("r_negative_degree_level")
    @Expose
    @Column
    protected Integer rNegativeDegreeLevel;
    @SerializedName("positive_letter_size")
    @Expose
    @Column
    protected Integer positiveLetterSize;
    @SerializedName("negative_letter_size")
    @Expose
    @Column
    protected Integer negativeLetterSize;

    public List<ReversalTrainBackDetail> reversal_train_back_details = new ArrayList<ReversalTrainBackDetail>();

    /**
     *
     * @return
     * The prescriptionAdjustStatus
     */
    public EnumPresAdjust getPrescriptionAdjustStatus() {
        return prescriptionAdjustStatus;
    }

    /**
     *
     * @param prescriptionAdjustStatus
     * The prescription_adjust_status
     */
    public void setPrescriptionAdjustStatus(EnumPresAdjust prescriptionAdjustStatus) {
        this.prescriptionAdjustStatus = prescriptionAdjustStatus;
    }

    public EnumEyeType getEyeType() {
        return eyeType;
    }

    public void setEyeType(EnumEyeType eyeType) {
        this.eyeType = eyeType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     *
     * @return
     * The lPositiveDegreeLevel
     */
    public Integer getLPositiveDegreeLevel() {
        return lPositiveDegreeLevel;
    }

    /**
     *
     * @param lPositiveDegreeLevel
     * The l_positive_degree_level
     */
    public void setLPositiveDegreeLevel(Integer lPositiveDegreeLevel) {
        this.lPositiveDegreeLevel = lPositiveDegreeLevel;
    }

    /**
     *
     * @return
     * The lNegativeDegreeLevel
     */
    public Integer getLNegativeDegreeLevel() {
        return lNegativeDegreeLevel;
    }

    /**
     *
     * @param lNegativeDegreeLevel
     * The l_negative_degree_level
     */
    public void setLNegativeDegreeLevel(Integer lNegativeDegreeLevel) {
        this.lNegativeDegreeLevel = lNegativeDegreeLevel;
    }

    /**
     *
     * @return
     * The rPositiveDegreeLevel
     */
    public Integer getRPositiveDegreeLevel() {
        return rPositiveDegreeLevel;
    }

    /**
     *
     * @param rPositiveDegreeLevel
     * The r_positive_degree_level
     */
    public void setRPositiveDegreeLevel(Integer rPositiveDegreeLevel) {
        this.rPositiveDegreeLevel = rPositiveDegreeLevel;
    }

    /**
     *
     * @return
     * The rNegativeDegreeLevel
     */
    public Integer getRNegativeDegreeLevel() {
        return rNegativeDegreeLevel;
    }

    /**
     *
     * @param rNegativeDegreeLevel
     * The r_negative_degree_level
     */
    public void setRNegativeDegreeLevel(Integer rNegativeDegreeLevel) {
        this.rNegativeDegreeLevel = rNegativeDegreeLevel;
    }

    /**
     *
     * @return
     * The positiveLetterSize
     */
    public Integer getPositiveLetterSize() {
        return positiveLetterSize;
    }

    /**
     *
     * @param positiveLetterSize
     * The positive_letter_size
     */
    public void setPositiveLetterSize(Integer positiveLetterSize) {
        this.positiveLetterSize = positiveLetterSize;
    }

    /**
     *
     * @return
     * The negativeLetterSize
     */
    public Integer getNegativeLetterSize() {
        return negativeLetterSize;
    }

    /**
     *
     * @param negativeLetterSize
     * The negative_letter_size
     */
    public void setNegativeLetterSize(Integer negativeLetterSize) {
        this.negativeLetterSize = negativeLetterSize;
    }

    public ReversalTrainBack(){
        this.setTrainItemType(EnumTrainItem.REVERSAL);
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            int prescription_adjust_status_value = jsonObject.getInt("prescription_adjust_status");
            setPrescriptionAdjustStatus(EnumPresAdjust.values()[prescription_adjust_status_value]);

            int eye_type_value = jsonObject.getInt("eye_type");
            setEyeType(EnumEyeType.values()[eye_type_value]);

            setScore(jsonObject.getInt("score"));

            setLPositiveDegreeLevel(jsonObject.getInt("l_positive_degree_level"));
            setLNegativeDegreeLevel(jsonObject.getInt("l_negative_degree_level"));
            setRPositiveDegreeLevel(jsonObject.getInt("r_positive_degree_level"));
            setRNegativeDegreeLevel(jsonObject.getInt("r_negative_degree_level"));

            setPositiveLetterSize(jsonObject.getInt("positive_letter_size"));
            setNegativeLetterSize(jsonObject.getInt("negative_letter_size"));

            JSONArray jsonArray = jsonObject.getJSONArray("reversal_train_back_details");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subobject=jsonArray.getJSONObject(i);
                ReversalTrainBackDetail detail = ReversalTrainBackDetail.convert(subobject);
                reversal_train_back_details.add(detail);
            }
        }catch (JSONException e){
            throw e;
        }
    }

    public static ReversalTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            ReversalTrainBack trainBack = new ReversalTrainBack();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("prescription_adjust_status",prescriptionAdjustStatus.toString());
            jsonObject.put("eye_type", eyeType.toString());
            jsonObject.put("score", score);
            jsonObject.put("l_positive_degree_level", lPositiveDegreeLevel);
            jsonObject.put("l_negative_degree_level", lNegativeDegreeLevel);
            jsonObject.put("r_positive_degree_level", rPositiveDegreeLevel);
            jsonObject.put("r_negative_degree_level", rNegativeDegreeLevel);
            jsonObject.put("positive_letter_size", positiveLetterSize);
            jsonObject.put("negative_letter_size", negativeLetterSize);

            JSONArray jsonArray = new JSONArray();
            for(int i=0;i< reversal_train_back_details.size();i++){
                JSONObject subobject = reversal_train_back_details.get(i).toJson();
                jsonArray.put(subobject);
            }
            jsonObject.put("reversal_train_back_details",jsonArray);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    /**
     * 细节个数加1
     * @param glass_type
     * @param level_id
     */
    public void detailCountAdd(EnumGlassType glass_type, int level_id){
        ReversalTrainBackDetail detail = null;
        int i=0;
        for(i=0;i<reversal_train_back_details.size();i++){
            detail = reversal_train_back_details.get(i);
            if(detail.getGlassType()==glass_type && detail.getLevelId()==level_id){
                break;
            }
        }
        //如果没有找到，生成新的
        if(i==reversal_train_back_details.size()){
            detail = new ReversalTrainBackDetail();
            detail.setReversalTrainBackId(getId());
            detail.setGlassType(glass_type);
            detail.setLevelId(level_id);
        }
        //个数加1
        detail.setPassCount(detail.getPassCount()+1);

        //如果没有找到，把新的添加进去
        if(i==reversal_train_back_details.size()){
            reversal_train_back_details.add(detail);
        }
    }

    /**
     * 计算得分
     * 为实最终级数的上一级的最高得分加上该级按比例的得分。
     */
    public void computeScore(int trainTimeSecond){
        int asthenopiaScore = ScoreManager.computeReversalTrain(eyeType,reversal_train_back_details,trainTimeSecond);
        int xscore = 100-asthenopiaScore;
        int glassLevel = lNegativeDegreeLevel;
        int startScore = 0;
        if(glassLevel>0){
            startScore = ReversalConfig.arrayLevelSectionScore[glassLevel-1];
        }
        int endScore = ReversalConfig.arrayLevelSectionScore[glassLevel];
        score = startScore + (int)((endScore-startScore)*xscore/100f);

        if(score<=1){
            score = 1;
        }
    }
}
