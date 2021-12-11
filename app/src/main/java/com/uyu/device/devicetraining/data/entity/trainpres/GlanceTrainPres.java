package com.uyu.device.devicetraining.data.entity.trainpres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/4.
 */
public class GlanceTrainPres extends TrainPres {
    @SerializedName("letter_size")
    @Expose
    private Integer letterSize;
    @SerializedName("letter_count")
    @Expose
    private Integer letterCount;
    @SerializedName("training_content_type")
    @Expose
    private EnumContentType trainingContentType;
    @SerializedName("training_content_article_id")
    @Expose
    private Integer trainingContentArticleId;

    /**
     *
     * @return
     * The letterSize
     */
    public Integer getLetterSize() {
        return letterSize;
    }

    /**
     *
     * @param letterSize
     * The letter_size
     */
    public void setLetterSize(Integer letterSize) {
        this.letterSize = letterSize;
    }

    /**
     *
     * @return
     * The letterCount
     */
    public Integer getLetterCount() {
        return letterCount;
    }

    /**
     *
     * @param letterCount
     * The letter_count
     */
    public void setLetterCount(Integer letterCount) {
        this.letterCount = letterCount;
    }

    /**
     *
     * @return
     * The trainingContentType
     */
    public EnumContentType getTrainingContentType() {
        return trainingContentType;
    }

    /**
     *
     * @param trainingContentType
     * The training_content_type
     */
    public void setTrainingContentType(EnumContentType trainingContentType) {
        this.trainingContentType = trainingContentType;
    }

    /**
     *
     * @return
     * The trainingContentArticleId
     */
    public Integer getTrainingContentArticleId() {
        return trainingContentArticleId;
    }

    /**
     *
     * @param trainingContentArticleId
     * The training_content_article_id
     */
    public void setTrainingContentArticleId(Integer trainingContentArticleId) {
        this.trainingContentArticleId = trainingContentArticleId;
    }

    public GlanceTrainPres(){
        this.setTrainItemType(EnumTrainItem.GLANCE);
    }

    public static GlanceTrainPres convert(JSONObject jsonObject) throws JSONException {
        try {
            GlanceTrainPres trainPres = new GlanceTrainPres();
            trainPres.convertParent(jsonObject);

            trainPres.setLetterSize(jsonObject.getInt("letter_size"));
            trainPres.setLetterCount(jsonObject.getInt("letter_count"));
            int training_content_type_value = jsonObject.getInt("training_content_type");
            trainPres.setTrainingContentType(EnumContentType.values()[training_content_type_value]);
            trainPres.setTrainingContentArticleId(jsonObject.getInt("training_content_article_id"));

            return trainPres;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("letter_size", letterSize);
            jsonObject.put("letter_count", letterCount);
            jsonObject.put("training_content_type", trainingContentType.getValue());
            jsonObject.put("training_content_article_id", trainingContentArticleId);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
