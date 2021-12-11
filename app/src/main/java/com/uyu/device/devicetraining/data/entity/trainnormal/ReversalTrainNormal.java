package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class ReversalTrainNormal extends ContentTrainNormal {
    public ReversalTrainNormal(){
        this.setTrainItemType(EnumTrainItem.REVERSAL);
    }

    public static ReversalTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            ReversalTrainNormal trainNormal = new ReversalTrainNormal();
            trainNormal.convertParent(jsonObject);
            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }
}
