package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.trainnormal.FracturedRulerTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.RGFixedVectorTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.RedGreenReadTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.ReversalTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.StereoscopeTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/5.
 */
public class NormalTrainMessageContent extends TrainMessageContent {
    @SerializedName("tn")
    @Expose
    private TrainNormal tn;

    public TrainNormal getTn() {
        return tn;
    }

    public void setTn(TrainNormal tn) {
        this.tn = tn;
    }

    public static NormalTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            NormalTrainMessageContent normalTrainMessageContent = new NormalTrainMessageContent();

            JSONObject tnValue = jsonObject.getJSONObject("tn");

            String train_item_type_value = tnValue.getString("train_item_type");
            EnumTrainItem trainItem = EnumTrainItem.getEnum(train_item_type_value);

            if(trainItem == EnumTrainItem.STEREOSCOPE){
                StereoscopeTrainNormal trainNormal = StereoscopeTrainNormal.convert(tnValue);
                normalTrainMessageContent.setTn(trainNormal);
            }else if(trainItem == EnumTrainItem.FRACTURED_RULER){
                FracturedRulerTrainNormal trainNormal = FracturedRulerTrainNormal.convert(tnValue);
                normalTrainMessageContent.setTn(trainNormal);
            }else if(trainItem == EnumTrainItem.REVERSAL){
                ReversalTrainNormal trainNormal = ReversalTrainNormal.convert(tnValue);
                normalTrainMessageContent.setTn(trainNormal);
            }else if(trainItem == EnumTrainItem.R_G_FIXED_VECTOR){
                RGFixedVectorTrainNormal trainNormal = RGFixedVectorTrainNormal.convert(tnValue);
                normalTrainMessageContent.setTn(trainNormal);
            }else if(trainItem == EnumTrainItem.RED_GREEN_READ){
                RedGreenReadTrainNormal trainNormal = RedGreenReadTrainNormal.convert(tnValue);
                normalTrainMessageContent.setTn(trainNormal);
            }

            return normalTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            JSONObject tnObject = tn.toJson();
            jsonObject.put("tn",tnObject);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
