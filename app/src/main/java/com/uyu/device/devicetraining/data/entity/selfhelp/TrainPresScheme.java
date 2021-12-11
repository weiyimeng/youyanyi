package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.other.ReceptionStatus;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2016/4/5.
 */
public class TrainPresScheme {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("optometrist_id")
    @Expose
    private Integer optometristId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("training_pres_list")
    @Expose
    private List<TrainPres> trainingPresList = new ArrayList<TrainPres>();

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
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The optometristId
     */
    public Integer getOptometristId() {
        return optometristId;
    }

    /**
     *
     * @param optometristId
     * The optometrist_id
     */
    public void setOptometristId(Integer optometristId) {
        this.optometristId = optometristId;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The trainingPresList
     */
    public List<TrainPres> getTrainingPresList() {
        return trainingPresList;
    }

    /**
     *
     * @param trainingPresList
     * The training_pres_list
     */
    public void setTrainingPresList(List<TrainPres> trainingPresList) {
        this.trainingPresList = trainingPresList;
    }

    public static TrainPresScheme convert(String strMsg){
        try {
            JSONObject jsonObject = new JSONObject(strMsg);
            return convert(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TrainPresScheme convert(JSONObject jsonObject){
        try {
            TrainPresScheme trainPresScheme = new TrainPresScheme();
            trainPresScheme.id = jsonObject.getInt("id");
            trainPresScheme.userId = jsonObject.getInt("user_id");
            trainPresScheme.optometristId = jsonObject.getInt("optometrist_id");
            trainPresScheme.createdAt = jsonObject.getString("created_at");
            trainPresScheme.updatedAt = jsonObject.getString("updated_at");
            JSONObject listObject = jsonObject.getJSONObject("training_pres_list");
            int subcount = listObject.getInt("subcount");
            for(int i=0;i<subcount;i++){
                JSONObject iobject = listObject.getJSONObject(String.valueOf(i));
                TrainPres trainPres = TrainPres.convert(iobject);
                trainPresScheme.trainingPresList.add(trainPres);
            }

            return trainPresScheme;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取下一个训练处方，可能是同一个小项的下一遍
     * @param receptionStatus
     * @return
     */
    public ReceptionStatus getNextTrainPres(ReceptionStatus receptionStatus){
        ReceptionStatus nextReceptionStatus = receptionStatus.deepClone();
        int step = nextReceptionStatus.getReceptStep();
        int detailStep = nextReceptionStatus.getReceptDetailStep();
        TrainPres nowTrainPres = trainingPresList.get(step);
        //detailStep循环遍数+1
        if(nowTrainPres.getTrainItemType()== EnumTrainItem.REVERSAL && ((ReversalTrainPres)nowTrainPres).getEyeType()== EnumEyeType.LEFT){
            int detailStepParam = nextReceptionStatus.getReceptDetailStepParam();

            if(detailStepParam== EnumEyeType.LEFT.getValue()){
                nextReceptionStatus.setReceptDetailStepParam(EnumEyeType.RIGHT.getValue());
            }else{
                nextReceptionStatus.setReceptDetailStepParam(EnumEyeType.LEFT.getValue());
                nextReceptionStatus.setReceptDetailStep(detailStep+1);
            }
        }else{
            if(nowTrainPres.getTrainItemType()== EnumTrainItem.REVERSAL && ((ReversalTrainPres)nowTrainPres).getEyeType()== EnumEyeType.RIGHT){
                ((ReversalTrainPres)nowTrainPres).setEyeType(EnumEyeType.LEFT);
                nextReceptionStatus.setReceptDetailStepParam(EnumEyeType.LEFT.getValue());
            }
            nextReceptionStatus.setReceptDetailStep(detailStep+1);
        }

        //遍数超过
        if(nextReceptionStatus.getReceptDetailStep()>nowTrainPres.getRepeatTrainingTimes()){
            if(step<trainingPresList.size()-1){
                //不是最后一个，继续下一个训练
                nextReceptionStatus.setReceptDetailStep(1);
                nextReceptionStatus.setReceptStep(step+1);
                nextReceptionStatus.setReceptDetailStepParam(EnumEyeType.LEFT.getValue());
            }else{
                //结束
                nextReceptionStatus = null;
            }
        }
        return nextReceptionStatus;
    }

    /**
     * 获取下一小项的训练处方
     * @param receptionStatus
     * @return
     */
    public ReceptionStatus getNextItemTrainPres(ReceptionStatus receptionStatus){
        ReceptionStatus nextReceptionStatus = receptionStatus.deepClone();
        int step = nextReceptionStatus.getReceptStep();
        int detailStep = nextReceptionStatus.getReceptDetailStep();

        if(step<trainingPresList.size()-1){
            //不是最后一个，继续下一个训练
            nextReceptionStatus.setReceptDetailStep(1);
            nextReceptionStatus.setReceptStep(step+1);
            nextReceptionStatus.setReceptDetailStepParam(EnumEyeType.LEFT.getValue());
        }else{
            //结束
            nextReceptionStatus = null;
        }

        return nextReceptionStatus;
    }
}
