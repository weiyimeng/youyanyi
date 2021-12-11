package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.AppProvider;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/4.
 */
public class RGFixedVectorTrainPres extends TrainPres {
    @SerializedName("training_type")
    @Expose
    private EnumFusionTrain trainingType;
    @SerializedName("dwell_time")
    @Expose
    private Integer dwellTime;
    /**
     * 起始等级，实际生活中表示的第一级
     */
    @SerializedName("start_difficulty")
    @Expose
    private Integer startDifficulty = 1;


    /**
     *
     * @return
     * The trainingType
     */
    public EnumFusionTrain getTrainingType() {
        return trainingType;
    }

    /**
     *
     * @param trainingType
     * The training_type
     */
    public void setTrainingType(EnumFusionTrain trainingType) {
        this.trainingType = trainingType;
    }

    /**
     *
     * @return
     * The dwellTime
     */
    public Integer getDwellTime() {
        return dwellTime;
    }

    /**
     *
     * @param dwellTime
     * The dwell_time
     */
    public void setDwellTime(Integer dwellTime) {
        this.dwellTime = dwellTime;
    }

    /**
     *
     * @return
     * The startDifficulty
     */
    public Integer getStartDifficulty() {
        return startDifficulty;
    }

    /**
     *
     * @param startDifficulty
     * The start_difficulty
     */
    public void setStartDifficulty(Integer startDifficulty) {
        this.startDifficulty = startDifficulty;
    }

    public RGFixedVectorTrainPres(){
        this.setTrainItemType(EnumTrainItem.R_G_FIXED_VECTOR);
    }

    public static RGFixedVectorTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            RGFixedVectorTrainPres trainPres = new RGFixedVectorTrainPres();
            trainPres.convertParent(jsonObject);

            int training_type_value = jsonObject.getInt("training_type");
            trainPres.setTrainingType(EnumFusionTrain.values()[training_type_value]);

            trainPres.setDwellTime(jsonObject.getInt("dwell_time"));

            //视光师发送的时候，不带这个字段
            if(jsonObject.has("start_difficulty")) {
                trainPres.setStartDifficulty(jsonObject.getInt("start_difficulty"));
            }

            return trainPres;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("training_type", trainingType.toString());
            jsonObject.put("dwell_time", dwellTime);
            jsonObject.put("start_difficulty", startDifficulty);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public String getShowName() {
        String showName = "";
        showName += trainItemType.getShowName();
        showName += trainingType.getShowName();
        return showName+ AppProvider.getApplication().getResources().getString(R.string.train);
    }
}
