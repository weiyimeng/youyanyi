package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class TrainNormal {
    @SerializedName("train_item_type")
    @Expose
    private EnumTrainItem trainItemType;

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
            String train_item_type_value = jsonObject.getString("train_item_type");
            trainItemType = EnumTrainItem.getEnum(train_item_type_value);
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("train_item_type",trainItemType.toString());

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
