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
public class RGVariableVectorTrainPres extends TrainPres {
    @SerializedName("training_type")
    @Expose
    private EnumFusionTrain trainingType;


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

    public RGVariableVectorTrainPres(){
        this.setTrainItemType(EnumTrainItem.R_G_VARIABLE_VECTOR);
    }

    public static RGVariableVectorTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            RGVariableVectorTrainPres trainPres = new RGVariableVectorTrainPres();
            trainPres.convertParent(jsonObject);

            int training_type_value = jsonObject.getInt("training_type");
            trainPres.setTrainingType(EnumFusionTrain.values()[training_type_value]);

            return trainPres;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("training_type", trainingType.toString());

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
