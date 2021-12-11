package com.uyu.device.devicetraining.data.entity.trainback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.InheritedColumn;
import com.raizlabs.android.dbflow.annotation.InheritedPrimaryKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.uyu.device.devicetraining.data.db.AppDatabase;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2015/12/8.
 */
@Table(database = AppDatabase.class,
        inheritedColumns = {@InheritedColumn(column = @Column, fieldName = "id")
                , @InheritedColumn(column = @Column, fieldName = "userId")
                , @InheritedColumn(column = @Column, fieldName = "optometristId")
                , @InheritedColumn(column = @Column, fieldName = "visionTrainingFeedbackId")
                , @InheritedColumn(column = @Column, fieldName = "itemTrainPresId")
                , @InheritedColumn(column = @Column, fieldName = "currentRepeatTime")
                , @InheritedColumn(column = @Column, fieldName = "trainingStartDate")
                , @InheritedColumn(column = @Column, fieldName = "trainingEndDate")
                , @InheritedColumn(column = @Column, fieldName = "totalTime")
                , @InheritedColumn(column = @Column, fieldName = "train_item_type")
        },
        inheritedPrimaryKeys = {@InheritedPrimaryKey(column = @Column,
                primaryKey = @PrimaryKey(autoincrement = true),
                fieldName = "localId")})
public class RedGreenReadTrainBack extends TrainBack {
    @SerializedName("letter_size")
    @Expose
    @Column
    private Integer letterSize;
    @SerializedName("letter_count")
    @Expose
    @Column
    private Integer letterCount;

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

    public RedGreenReadTrainBack(){
        this.setTrainItemType(EnumTrainItem.RED_GREEN_READ);
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            setLetterSize(jsonObject.getInt("letter_size"));
            setLetterCount(jsonObject.getInt("letter_count"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static RedGreenReadTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            RedGreenReadTrainBack trainBack = new RedGreenReadTrainBack();
            trainBack.convertParent(jsonObject);
            
            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("letter_size", letterSize);
            jsonObject.put("letter_count", letterCount);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
