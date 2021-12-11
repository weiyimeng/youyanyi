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
public class FollowTrainBack extends TrainBack {
    @SerializedName("accuracy")
    @Expose
    @Column
    private Double accuracy = 0d;

    /**
     *
     * @return
     * The accuracy
     */
    public Double getAccuracy() {
        return accuracy;
    }

    /**
     *
     * @param accuracy
     * The accuracy
     */
    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }


    public FollowTrainBack(){
        this.setTrainItemType(EnumTrainItem.FOLLOW);
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            setAccuracy(jsonObject.getDouble("accuracy"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static FollowTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            FollowTrainBack trainBack = new FollowTrainBack();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("accuracy", accuracy);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
