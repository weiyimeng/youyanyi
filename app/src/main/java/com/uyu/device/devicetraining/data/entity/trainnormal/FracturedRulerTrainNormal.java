package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class FracturedRulerTrainNormal extends LevelTrainNormal {
    public FracturedRulerTrainNormal(){
        this.setTrainItemType(EnumTrainItem.FRACTURED_RULER);
    }

    public static FracturedRulerTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            FracturedRulerTrainNormal trainNormal = new FracturedRulerTrainNormal();
            trainNormal.convertParent(jsonObject);
            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }
}
