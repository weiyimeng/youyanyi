package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.AppProvider;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/4.
 */
public class TrainPres {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("training_prescription_scheme_id")
    @Expose
    private Integer trainingPrescriptionSchemeId;
    @SerializedName("scheme_process_num")
    @Expose
    private Integer schemeProcessNum;
    @SerializedName("repeat_training_times")
    @Expose
    private Integer repeatTrainingTimes;
    @SerializedName("train_item_type")
    @Expose
    protected EnumTrainItem trainItemType;

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
     * The trainingPrescriptionSchemeId
     */
    public Integer getTrainingPrescriptionSchemeId() {
        return trainingPrescriptionSchemeId;
    }

    /**
     *
     * @param trainingPrescriptionSchemeId
     * The training_prescription_scheme_id
     */
    public void setTrainingPrescriptionSchemeId(Integer trainingPrescriptionSchemeId) {
        this.trainingPrescriptionSchemeId = trainingPrescriptionSchemeId;
    }

    /**
     *
     * @return
     * The schemeProcessNum
     */
    public Integer getSchemeProcessNum() {
        return schemeProcessNum;
    }

    /**
     *
     * @param schemeProcessNum
     * The scheme_process_num
     */
    public void setSchemeProcessNum(Integer schemeProcessNum) {
        this.schemeProcessNum = schemeProcessNum;
    }

    /**
     *
     * @return
     * The repeatTrainingTimes
     */
    public Integer getRepeatTrainingTimes() {
        return repeatTrainingTimes;
    }

    /**
     *
     * @param repeatTrainingTimes
     * The repeat_training_times
     */
    public void setRepeatTrainingTimes(Integer repeatTrainingTimes) {
        this.repeatTrainingTimes = repeatTrainingTimes;
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

    public static TrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            String train_item_type_value = jsonObject.getString("train_item_type");
            EnumTrainItem trainItem = EnumTrainItem.getEnum(train_item_type_value);

            TrainPres trainPres = null;
            if (trainItem == EnumTrainItem.STEREOSCOPE) {
                trainPres = StereoscopeTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.FRACTURED_RULER) {
                trainPres = FracturedRulerTrainPres.convert(jsonObject);
            }else if(trainItem == EnumTrainItem.REVERSAL){
                trainPres = ReversalTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.RED_GREEN_READ) {
                trainPres = RedGreenReadTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.APPROACH) {
                trainPres = ApproachTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.R_G_VARIABLE_VECTOR) {
                trainPres = RGVariableVectorTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.R_G_FIXED_VECTOR) {
                trainPres = RGFixedVectorTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.GLANCE) {
                trainPres = GlanceTrainPres.convert(jsonObject);
            }else if (trainItem == EnumTrainItem.FOLLOW) {
                trainPres = FollowTrainPres.convert(jsonObject);
            }

            return trainPres;
        } catch (JSONException e) {
            throw e;
        }
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            id = jsonObject.getInt("id");
            trainingPrescriptionSchemeId = jsonObject.getInt("training_prescription_scheme_id");
            schemeProcessNum = jsonObject.getInt("scheme_process_num");
            repeatTrainingTimes = jsonObject.getInt("repeat_training_times");
            String train_item_type_value = jsonObject.getString("train_item_type");
            trainItemType = EnumTrainItem.getEnum(train_item_type_value);
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("training_prescription_scheme_id",trainingPrescriptionSchemeId);
            jsonObject.put("scheme_process_num",schemeProcessNum);
            jsonObject.put("repeat_training_times",repeatTrainingTimes);
            jsonObject.put("train_item_type",trainItemType.toString());

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    public String getShowName(){
        String showName = "";
        showName += trainItemType.getShowName()+ AppProvider.getApplication().getResources().getString(R.string.train);
        return showName;
    }

    /**
     * 是否相同的器械
     * @param trainPresCompare
     * @return
     */
    public boolean isSameInstrument(TrainPres trainPresCompare){
        boolean isSame = false;
        if(trainPresCompare!=null) {
            if (trainItemType == trainPresCompare.getTrainItemType()) {
                if (trainItemType == EnumTrainItem.FRACTURED_RULER) {
                    if (((FracturedRulerTrainPres) this).getTrainingType() == ((FracturedRulerTrainPres) trainPresCompare).getTrainingType()) {
                        //是裂隙尺，且融合项目相同
                        isSame = true;
                    }
                } else {
                    //非裂隙尺肯定相同
                    isSame = true;
                }
            }
        }
        return isSame;
    }
}
