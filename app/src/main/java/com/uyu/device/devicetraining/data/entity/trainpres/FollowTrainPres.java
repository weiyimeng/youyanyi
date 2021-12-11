package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumLineType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/4.
 */
public class FollowTrainPres extends TrainPres {
    @SerializedName("line_type")
    @Expose
    private EnumLineType lineType;
    @SerializedName("line_count")
    @Expose
    private Integer lineCount;
    @SerializedName("pic_count")
    @Expose
    private Integer picCount;

    /**
     *
     * @return
     * The lineType
     */
    public EnumLineType getLineType() {
        return lineType;
    }

    /**
     *
     * @param lineType
     * The line_type
     */
    public void setLineType(EnumLineType lineType) {
        this.lineType = lineType;
    }

    /**
     *
     * @return
     * The lineCount
     */
    public Integer getLineCount() {
        return lineCount;
    }

    /**
     *
     * @param lineCount
     * The line_count
     */
    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    /**
     *
     * @return
     * The picCount
     */
    public Integer getPicCount() {
        return picCount;
    }

    /**
     *
     * @param picCount
     * The pic_count
     */
    public void setPicCount(Integer picCount) {
        this.picCount = picCount;
    }

    public FollowTrainPres(){
        this.setTrainItemType(EnumTrainItem.FOLLOW);
    }

    public static FollowTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            FollowTrainPres trainPres = new FollowTrainPres();
            trainPres.convertParent(jsonObject);

            int line_type_value = jsonObject.getInt("line_type");
            trainPres.setLineType(EnumLineType.values()[line_type_value]);
            trainPres.setLineCount(jsonObject.getInt("line_count"));
            trainPres.setPicCount(jsonObject.getInt("pic_count"));

            return trainPres;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("line_type", lineType.toString());
            jsonObject.put("line_count", lineCount);
            jsonObject.put("pic_count", picCount);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
