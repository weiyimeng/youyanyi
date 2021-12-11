package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.trainback.ApproachTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FollowTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.GlanceTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RedGreenReadTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/5.
 */
public class BackTrainMessageContent extends TrainMessageContent {
    @SerializedName("tb")
    @Expose
    private TrainBack tb;

    public TrainBack getTb() {
        return tb;
    }

    public void setTb(TrainBack tb) {
        this.tb = tb;
    }

    public static BackTrainMessageContent convert(JSONObject jsonObject) throws JSONException {
        try {
            BackTrainMessageContent backTrainMessageContent = new BackTrainMessageContent();

            JSONObject tbValue = jsonObject.getJSONObject("tb");

            String train_item_type_value = tbValue.getString("train_item_type");
            EnumTrainItem trainItem = EnumTrainItem.getEnum(train_item_type_value);

            if (trainItem == EnumTrainItem.STEREOSCOPE){
                StereoscopeTrainBack trainBack = StereoscopeTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.FRACTURED_RULER) {
                FracturedRulerTrainBack trainBack = FracturedRulerTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.REVERSAL) {
                ReversalTrainBack trainBack = ReversalTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.RED_GREEN_READ){
                RedGreenReadTrainBack trainBack = RedGreenReadTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.APPROACH){
                ApproachTrainBack trainBack = ApproachTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.R_G_VARIABLE_VECTOR){
                RGVariableVectorTrainBack trainBack = RGVariableVectorTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.R_G_FIXED_VECTOR){
                RGFixedVectorTrainBack trainBack = RGFixedVectorTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.GLANCE){
                GlanceTrainBack trainBack = GlanceTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }else if (trainItem == EnumTrainItem.FOLLOW){
                FollowTrainBack trainBack = FollowTrainBack.convert(tbValue);
                backTrainMessageContent.setTb(trainBack);
            }

            return backTrainMessageContent;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            JSONObject tbObject = tb.toJson();
            jsonObject.put("tb",tbObject);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
