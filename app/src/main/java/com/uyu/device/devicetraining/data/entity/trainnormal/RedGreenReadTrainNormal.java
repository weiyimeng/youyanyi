package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class RedGreenReadTrainNormal extends ContentTrainNormal {
    public RedGreenReadTrainNormal(){
        this.setTrainItemType(EnumTrainItem.RED_GREEN_READ);
    }

    public static RedGreenReadTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            RedGreenReadTrainNormal trainNormal = new RedGreenReadTrainNormal();
            trainNormal.convertParent(jsonObject);
            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }
}
