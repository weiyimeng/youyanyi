package com.uyu.device.devicetraining.data.entity.other;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/3/15.
 */
public class ReceptionStatus {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("recept_step")
    @Expose
    private Integer receptStep;
    @SerializedName("recept_detail_step")
    @Expose
    private Integer receptDetailStep;
    @SerializedName("recept_detail_step_param")
    @Expose
    private Integer receptDetailStepParam;
    @SerializedName("recept_detail_step_status")
    @Expose
    private Integer receptDetailStepStatus;

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
     * The receptStep
     */
    public Integer getReceptStep() {
        return receptStep;
    }

    /**
     *
     * @param receptStep
     * The recept_step
     */
    public void setReceptStep(Integer receptStep) {
        this.receptStep = receptStep;
    }

    /**
     *
     * @return
     * The receptDetailStep
     */
    public Integer getReceptDetailStep() {
        return receptDetailStep;
    }

    /**
     *
     * @param receptDetailStep
     * The recept_detail_step
     */
    public void setReceptDetailStep(Integer receptDetailStep) {
        this.receptDetailStep = receptDetailStep;
    }

    /**
     *
     * @return
     * The receptDetailStepParam
     */
    public Integer getReceptDetailStepParam() {
        return receptDetailStepParam;
    }

    /**
     *
     * @param receptDetailStepParam
     * The recept_detail_step_param
     */
    public void setReceptDetailStepParam(Integer receptDetailStepParam) {
        this.receptDetailStepParam = receptDetailStepParam;
    }

    /**
     *
     * @return
     * The receptDetailStepStatus
     */
    public Integer getReceptDetailStepStatus() {
        return receptDetailStepStatus;
    }

    /**
     *
     * @param receptDetailStepStatus
     * The recept_detail_step_status
     */
    public void setReceptDetailStepStatus(Integer receptDetailStepStatus) {
        this.receptDetailStepStatus = receptDetailStepStatus;
    }

    /**
     * 深拷贝
     * @return
     */
    public ReceptionStatus deepClone(){
        ReceptionStatus receptionStatus = new ReceptionStatus();
        receptionStatus.id = id;
        receptionStatus.receptStep = receptStep;
        receptionStatus.receptDetailStep = receptDetailStep;
        receptionStatus.receptDetailStepParam = receptDetailStepParam;
        receptionStatus.receptDetailStepStatus = receptDetailStepStatus;
        return receptionStatus;
    }
}
