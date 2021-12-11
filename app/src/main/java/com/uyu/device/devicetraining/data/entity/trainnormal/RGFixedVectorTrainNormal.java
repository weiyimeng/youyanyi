package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class RGFixedVectorTrainNormal extends LevelTrainNormal {
    public RGFixedVectorTrainNormal(){
        this.setTrainItemType(EnumTrainItem.R_G_FIXED_VECTOR);
    }

    public static RGFixedVectorTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            RGFixedVectorTrainNormal trainNormal = new RGFixedVectorTrainNormal();
            trainNormal.convertParent(jsonObject);
            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }
}
