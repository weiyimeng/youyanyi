package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/4.
 */
public class ApproachTrainPres extends TrainPres {
    @SerializedName("dwell_time")
    @Expose
    private Integer dwellTime;

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

    public ApproachTrainPres(){
        this.setTrainItemType(EnumTrainItem.APPROACH);
    }

    public static ApproachTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            ApproachTrainPres trainPres = new ApproachTrainPres();
            trainPres.convertParent(jsonObject);

            trainPres.setDwellTime(jsonObject.getInt("dwell_time"));

            return trainPres;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("dwell_time", dwellTime);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
