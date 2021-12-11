package com.uyu.device.devicetraining.data.entity.trainback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/8.
 */
public class ReversalTrainBackDetail{
    @SerializedName("id")
    @Expose
    private Integer id=0;
    @SerializedName("reversal_train_back_id")
    @Expose
    private Integer reversalTrainBackId=0;
    @SerializedName("glass_type")
    @Expose
    private EnumGlassType glassType= EnumGlassType.Zheng;
    @SerializedName("level_id")
    @Expose
    private Integer levelId=0;
    @SerializedName("pass_count")
    @Expose
    private Integer passCount=0;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The reversalTrainBackId
     */
    public Integer getReversalTrainBackId() {
        return reversalTrainBackId;
    }

    /**
     *
     * @param reversalTrainBackId
     * The reversal_train_back_id
     */
    public void setReversalTrainBackId(Integer reversalTrainBackId) {
        this.reversalTrainBackId = reversalTrainBackId;
    }

    /**
     *
     * @return
     * The glassType
     */
    public EnumGlassType getGlassType() {
        return glassType;
    }

    /**
     *
     * @param glassType
     * The glass_type
     */
    public void setGlassType(EnumGlassType glassType) {
        this.glassType = glassType;
    }

    /**
     *
     * @return
     * The levelId
     */
    public Integer getLevelId() {
        return levelId;
    }

    /**
     *
     * @param levelId
     * The level_id
     */
    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    /**
     *
     * @return
     * The passCount
     */
    public Integer getPassCount() {
        return passCount;
    }

    /**
     *
     * @param passCount
     * The pass_count
     */
    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            setId(jsonObject.getInt("id"));
            setReversalTrainBackId(jsonObject.getInt("reversal_train_back_id"));

            int glass_type_value = jsonObject.getInt("glass_type");
            setGlassType(EnumGlassType.values()[glass_type_value]);

            setLevelId(jsonObject.getInt("level_id"));
            setPassCount(jsonObject.getInt("pass_count"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static ReversalTrainBackDetail convert(JSONObject jsonObject) throws JSONException {
        try {
            ReversalTrainBackDetail trainBack = new ReversalTrainBackDetail();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("reversal_train_back_id",reversalTrainBackId);
            jsonObject.put("glass_type",glassType.toString());
            jsonObject.put("level_id", levelId);
            jsonObject.put("pass_count", passCount);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
