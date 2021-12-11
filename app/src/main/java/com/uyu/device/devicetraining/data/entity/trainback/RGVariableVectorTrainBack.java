package com.uyu.device.devicetraining.data.entity.trainback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.InheritedColumn;
import com.raizlabs.android.dbflow.annotation.InheritedPrimaryKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.uyu.device.devicetraining.data.db.AppDatabase;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
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
public class RGVariableVectorTrainBack extends TrainBack {
    @SerializedName("training_type")
    @Expose
    @Column
    protected EnumFusionTrain trainingType;
    @SerializedName("critical_location")
    @Expose
    @Column
    protected Double criticalLocation;

    /**
     *
     * @return
     * The trainingType
     */
    public EnumFusionTrain getTrainingType() {
        return trainingType;
    }

    /**
     *
     * @param trainingType
     * The training_type
     */
    public void setTrainingType(EnumFusionTrain trainingType) {
        this.trainingType = trainingType;
    }

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

    public RGVariableVectorTrainBack(){
        this.setTrainItemType(EnumTrainItem.R_G_VARIABLE_VECTOR);
    }

    public void convertParent(JSONObject jsonObject) throws JSONException {
        try {
            super.convertParent(jsonObject);

            int training_type_value = jsonObject.getInt("training_type");
            setTrainingType(EnumFusionTrain.values()[training_type_value]);

            setCriticalLocation(jsonObject.getDouble("critical_location"));
        }catch (JSONException e){
            throw e;
        }
    }

    public static RGVariableVectorTrainBack convert(JSONObject jsonObject) throws JSONException {
        try {
            RGVariableVectorTrainBack trainBack = new RGVariableVectorTrainBack();
            trainBack.convertParent(jsonObject);

            return trainBack;
        }catch (JSONException e){
            throw e;
        }
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("training_type",trainingType.toString());
            jsonObject.put("critical_location", criticalLocation);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
