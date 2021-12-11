package com.uyu.device.devicetraining.data.entity.trainnormal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/23.
 */
public class ContentTrainNormal extends TrainNormal {
    @SerializedName("train_content")
    @Expose
    private String trainContent;

    /**
     *
     * @return
     * The trainContent
     */
    public String getTrainContent() {
        return trainContent;
    }

    /**
     *
     * @param trainContent
     * The train_content
     */
    public void setTrainContent(String trainContent) {
        this.trainContent = trainContent;
    }

    public ContentTrainNormal(){
        this.setTrainItemType(EnumTrainItem.REVERSAL);
    }

    public static ContentTrainNormal convert(JSONObject jsonObject) throws JSONException {
        try {
            ContentTrainNormal trainNormal = new ContentTrainNormal();
            trainNormal.convertParent(jsonObject);

            trainNormal.setTrainContent(jsonObject.getString("train_content"));

            return trainNormal;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("train_content", trainContent);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);
            setTrainContent(jsonObject.getString("train_content"));
        }catch (JSONException e){
            throw e;
        }
    }
}
