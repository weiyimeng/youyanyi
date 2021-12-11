package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumContentLoopMode;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/4.
 */
public class ReversalTrainPres extends TrainPres{
    @SerializedName("eye_type")
    @Expose
    private EnumEyeType eyeType;
    @SerializedName("loop_unit_type")
    @Expose
    private Integer loopUnitType;
    @SerializedName("l_positive_degree_level")
    @Expose
    private Integer lPositiveDegreeLevel;
    @SerializedName("l_negative_degree_level")
    @Expose
    private Integer lNegativeDegreeLevel;
    @SerializedName("r_positive_degree_level")
    @Expose
    private Integer rPositiveDegreeLevel;
    @SerializedName("r_negative_degree_level")
    @Expose
    private Integer rNegativeDegreeLevel;
    @SerializedName("positive_letter_size")
    @Expose
    private Integer positiveLetterSize;
    @SerializedName("negative_letter_size")
    @Expose
    private Integer negativeLetterSize;
    @SerializedName("positive_letter_size_right")
    @Expose
    private Integer positiveLetterSizeRight;
    @SerializedName("negative_letter_size_right")
    @Expose
    private Integer negativeLetterSizeRight;
    @SerializedName("loop_positive_num")
    @Expose
    private Integer loopPositiveNum;
    @SerializedName("loop_negative_num")
    @Expose
    private Integer loopNegativeNum;
    @SerializedName("loop_positive_num_right")
    @Expose
    private Integer loopPositiveNumRight;
    @SerializedName("loop_negative_num_right")
    @Expose
    private Integer loopNegativeNumRight;
    @SerializedName("training_duration")
    @Expose
    private Integer trainingDuration;
    @SerializedName("training_content_type")
    @Expose
    private EnumContentType trainingContentType=EnumContentType.LETTER;
    @SerializedName("training_content_category_id")
    @Expose
    private Integer trainingContentCategoryId=0;
    @SerializedName("training_content_article_id")
    @Expose
    private Integer trainingContentArticleId=0;
    @SerializedName("training_content_loop_mode")
    @Expose
    private EnumContentLoopMode trainingContentLoopMode=EnumContentLoopMode.SINGLE;

    /**
     *
     * @return
     * The eyeType
     */
    public EnumEyeType getEyeType() {
        return eyeType;
    }

    /**
     *
     * @param eyeType
     * The eye_type
     */
    public void setEyeType(EnumEyeType eyeType) {
        this.eyeType = eyeType;
    }

    /**
     *
     * @return
     * The loopUnitType
     */
    public Integer getLoopUnitType() {
        return loopUnitType;
    }

    /**
     *
     * @param loopUnitType
     * The loop_unit_type
     */
    public void setLoopUnitType(Integer loopUnitType) {
        this.loopUnitType = loopUnitType;
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

    /**
     *
     * @return
     * The positiveLetterSizeRight
     */
    public Integer getPositiveLetterSizeRight() {
        return positiveLetterSizeRight;
    }

    /**
     *
     * @param positiveLetterSizeRight
     * The positive_letter_size_right
     */
    public void setPositiveLetterSizeRight(Integer positiveLetterSizeRight) {
        this.positiveLetterSizeRight = positiveLetterSizeRight;
    }

    /**
     *
     * @return
     * The negativeLetterSizeRight
     */
    public Integer getNegativeLetterSizeRight() {
        return negativeLetterSizeRight;
    }

    /**
     *
     * @param negativeLetterSizeRight
     * The negative_letter_size_right
     */
    public void setNegativeLetterSizeRight(Integer negativeLetterSizeRight) {
        this.negativeLetterSizeRight = negativeLetterSizeRight;
    }

    /**
     *
     * @return
     * The loopPositiveNum
     */
    public Integer getLoopPositiveNum() {
        return loopPositiveNum;
    }

    /**
     *
     * @param loopPositiveNum
     * The loop_positive_num
     */
    public void setLoopPositiveNum(Integer loopPositiveNum) {
        this.loopPositiveNum = loopPositiveNum;
    }

    /**
     *
     * @return
     * The loopNegativeNum
     */
    public Integer getLoopNegativeNum() {
        return loopNegativeNum;
    }

    /**
     *
     * @param loopNegativeNum
     * The loop_negative_num
     */
    public void setLoopNegativeNum(Integer loopNegativeNum) {
        this.loopNegativeNum = loopNegativeNum;
    }

    /**
     *
     * @return
     * The loopPositiveNumRight
     */
    public Integer getLoopPositiveNumRight() {
        return loopPositiveNumRight;
    }

    /**
     *
     * @param loopPositiveNumRight
     * The loop_positive_num_right
     */
    public void setLoopPositiveNumRight(Integer loopPositiveNumRight) {
        this.loopPositiveNumRight = loopPositiveNumRight;
    }

    /**
     *
     * @return
     * The loopNegativeNumRight
     */
    public Integer getLoopNegativeNumRight() {
        return loopNegativeNumRight;
    }

    /**
     *
     * @param loopNegativeNumRight
     * The loop_negative_num_right
     */
    public void setLoopNegativeNumRight(Integer loopNegativeNumRight) {
        this.loopNegativeNumRight = loopNegativeNumRight;
    }

    /**
     *
     * @return
     * The trainingDuration
     */
    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    /**
     *
     * @param trainingDuration
     * The training_duration
     */
    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    /**
     *
     * @return
     * The trainingContentType
     */
    public EnumContentType getTrainingContentType() {
        return trainingContentType;
    }

    /**
     *
     * @param trainingContentType
     * The training_content_type
     */
    public void setTrainingContentType(EnumContentType trainingContentType) {
        this.trainingContentType = trainingContentType;
    }

    /**
     *
     * @return
     * The trainingContentCategoryId
     */
    public Integer getTrainingContentCategoryId() {
        return trainingContentCategoryId;
    }

    /**
     *
     * @param trainingContentCategoryId
     * The training_content_category_id
     */
    public void setTrainingContentCategoryId(Integer trainingContentCategoryId) {
        this.trainingContentCategoryId = trainingContentCategoryId;
    }

    /**
     *
     * @return
     * The trainingContentArticleId
     */
    public Integer getTrainingContentArticleId() {
        return trainingContentArticleId;
    }

    /**
     *
     * @param trainingContentArticleId
     * The training_content_article_id
     */
    public void setTrainingContentArticleId(Integer trainingContentArticleId) {
        this.trainingContentArticleId = trainingContentArticleId;
    }

    /**
     *
     * @return
     * The trainingContentType
     */
    public EnumContentLoopMode getTrainingContentLoopMode() {
        return trainingContentLoopMode;
    }

    /**
     *
     * @param trainingContentLoopMode
     * The training_content_loop_mode
     */
    public void setTrainingContentLoopMode(EnumContentLoopMode trainingContentLoopMode) {
        this.trainingContentLoopMode = trainingContentLoopMode;
    }

    public ReversalTrainPres(){
        this.setTrainItemType(EnumTrainItem.REVERSAL);
    }

    public static ReversalTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            ReversalTrainPres trainMessage = new ReversalTrainPres();
            trainMessage.convertParent(jsonObject);

            int eye_type_value = jsonObject.getInt("eye_type");
            trainMessage.setEyeType(EnumEyeType.values()[eye_type_value]);

            trainMessage.setLoopUnitType(jsonObject.getInt("loop_unit_type"));
            trainMessage.setLPositiveDegreeLevel(jsonObject.getInt("l_positive_degree_level"));
            trainMessage.setLNegativeDegreeLevel(jsonObject.getInt("l_negative_degree_level"));
            trainMessage.setRPositiveDegreeLevel(jsonObject.getInt("r_positive_degree_level"));
            trainMessage.setRNegativeDegreeLevel(jsonObject.getInt("r_negative_degree_level"));
            trainMessage.setPositiveLetterSize(jsonObject.getInt("positive_letter_size"));
            trainMessage.setNegativeLetterSize(jsonObject.getInt("negative_letter_size"));
            trainMessage.setPositiveLetterSizeRight(jsonObject.getInt("positive_letter_size_right"));
            trainMessage.setNegativeLetterSizeRight(jsonObject.getInt("negative_letter_size_right"));
            trainMessage.setLoopPositiveNum(jsonObject.getInt("loop_positive_num"));
            trainMessage.setLoopNegativeNum(jsonObject.getInt("loop_negative_num"));
            trainMessage.setLoopPositiveNumRight(jsonObject.getInt("loop_positive_num_right"));
            trainMessage.setLoopNegativeNumRight(jsonObject.getInt("loop_negative_num_right"));
            trainMessage.setTrainingDuration(jsonObject.getInt("training_duration"));

            int training_content_type_value = jsonObject.getInt("training_content_type");
            trainMessage.setTrainingContentType(EnumContentType.values()[training_content_type_value]);
            trainMessage.setTrainingContentCategoryId(jsonObject.getInt("training_content_category_id"));
            trainMessage.setTrainingContentArticleId(jsonObject.getInt("training_content_article_id"));

            int training_content_loop_mode_value = jsonObject.getInt("training_content_loop_mode");
            trainMessage.setTrainingContentLoopMode(EnumContentLoopMode.values()[training_content_loop_mode_value]);

            return trainMessage;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("eye_type", eyeType.getValue());
            jsonObject.put("loop_unit_type", loopUnitType);
            jsonObject.put("l_positive_degree_level", lPositiveDegreeLevel);
            jsonObject.put("l_negative_degree_level", lNegativeDegreeLevel);
            jsonObject.put("r_positive_degree_level", rPositiveDegreeLevel);
            jsonObject.put("r_negative_degree_level", rNegativeDegreeLevel);
            jsonObject.put("positive_letter_size", positiveLetterSize);
            jsonObject.put("negative_letter_size", negativeLetterSize);
            jsonObject.put("positive_letter_size_right", positiveLetterSizeRight);
            jsonObject.put("negative_letter_size_right", negativeLetterSizeRight);
            jsonObject.put("loop_positive_num", loopPositiveNum);
            jsonObject.put("loop_negative_num", loopNegativeNum);
            jsonObject.put("loop_positive_num_right", loopPositiveNumRight);
            jsonObject.put("loop_negative_num_right", loopNegativeNumRight);
            jsonObject.put("training_duration", trainingDuration);
            jsonObject.put("training_content_type", trainingContentType.getValue());
            jsonObject.put("training_content_category_id", trainingContentCategoryId);
            jsonObject.put("training_content_article_id", trainingContentArticleId);
            jsonObject.put("training_content_loop_mode", trainingContentLoopMode.getValue());

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public String getShowName() {
        String showName = "";
        showName += trainItemType.getShowName();
        if(eyeType==EnumEyeType.DOUBLE){
            showName += AppProvider.getApplication().getResources().getString(R.string.eye_double);
        }else{
            showName += AppProvider.getApplication().getResources().getString(R.string.monocular);
        }
        showName += AppProvider.getApplication().getResources().getString(R.string.train);
        return showName;
    }

    /**
     * 处方是否完全一样
     * @return
     */
    public static boolean isCompletedEqual(ReversalTrainPres previousTrainPres,ReversalTrainPres trainPres){
        boolean isSame = (previousTrainPres!=null && trainPres!=null
                && previousTrainPres.getId().intValue() == trainPres.getId().intValue()
                && previousTrainPres.getTrainingContentType() == trainPres.getTrainingContentType()
                && previousTrainPres.getTrainingContentCategoryId().intValue() == trainPres.getTrainingContentCategoryId().intValue()
                && previousTrainPres.getTrainingContentArticleId().intValue()== trainPres.getTrainingContentArticleId().intValue());
        return isSame;
    }

    /**
     * 是否同一方案下的内容相同
     * @param previousTrainPres
     * @param trainPres
     * @return
     */
    public static boolean isContentEqual(ReversalTrainPres previousTrainPres,ReversalTrainPres trainPres){
        boolean isSame = (previousTrainPres!=null && trainPres!=null
                && previousTrainPres.getTrainingPrescriptionSchemeId().intValue() == trainPres.getTrainingPrescriptionSchemeId().intValue()
                && previousTrainPres.getTrainingContentType() == trainPres.getTrainingContentType());

        if(trainPres.getTrainingContentType()==EnumContentType.ARTICLE){
            isSame = isSame && previousTrainPres.getTrainingContentArticleId().intValue()== trainPres.getTrainingContentArticleId().intValue();
        }else if(trainPres.getTrainingContentType()==EnumContentType.NEWS){
            isSame = isSame && previousTrainPres.getTrainingContentCategoryId().intValue() == trainPres.getTrainingContentCategoryId().intValue();
        }

        return isSame;
    }
}
