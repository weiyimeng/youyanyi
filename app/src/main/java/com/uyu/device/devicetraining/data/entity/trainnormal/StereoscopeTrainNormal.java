package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class StereoscopeTrainNormal extends LevelTrainNormal {
    public StereoscopeTrainNormal(){
        this.setTrainItemType(EnumTrainItem.STEREOSCOPE);
    }

    public static StereoscopeTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            StereoscopeTrainNormal trainNormal = new StereoscopeTrainNormal();
            trainNormal.convertParent(jsonObject);
            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }
}
