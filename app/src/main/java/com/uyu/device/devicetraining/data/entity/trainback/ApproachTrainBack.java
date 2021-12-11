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
public class ApproachTrainBack extends TrainBack {
    @SerializedName("critical_location")
    @Expose
    @Column
    private Double criticalLocation;
    @SerializedName("real_dwell_time")
    @Expose
    @Column
    private Integer realDwellTime;
    @SerializedName("eye_change_type")
    @Expose
    @Column
    private Integer eyeChangeType;

    /**
     *
     * @return
     * The criticalLocation
     */
    public Double getCriticalLocation() {
        return criticalLocation;
    }

    /**
     *
     * @param criticalLocation
     * The critical_location
     */
    public void setCriticalLocation(Double criticalLocation) {
        this.criticalLocation = criticalLocation;
    }

    /**
     *
     * @return
     * The realDwellTime
     */
    public Integer getRealDwellTime() {
        return realDwellTime;
    }

    /**
     *
     * @param realDwellTime
     * The real_dwell_time
     */
    public void setRealDwellTime(Integer realDwellTime) {
        this.realDwellTime = realDwellTime;
    }

    /**
     *
     * @return
     * The eyeChangeType
     */
    public Integer getEyeChangeType() {
        return eyeChangeType;
    }

    /**
     *
     * @param eyeChangeType
     * The eye_change_type
     */
    public void setEyeChangeType(Integer eyeChangeType) {
        this.eyeChangeType = eyeChangeType;
    }

    public ApproachTrainBack(){
        this.setTrainItemType(EnumTrainItem.APPROACH);
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            setCriticalLocation(jsonObject.getDouble("critical_location"));
            setRealDwellTime(jsonObject.getInt("real_dwell_time"));
            setEyeChangeType(jsonObject.getInt("eye_change_type"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static ApproachTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            ApproachTrainBack trainBack = new ApproachTrainBack();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("critical_location",criticalLocation);
            jsonObject.put("real_dwell_time", realDwellTime);
            jsonObject.put("eye_change_type", eyeChangeType);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
