package com.uyu.device.devicetraining.data.entity.other;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.uyu.device.devicetraining.data.db.AppDatabase;

/**
 * Created by windern on 2016/3/15.
 */
@Table(database = AppDatabase.class)
public class Reception extends BaseModel {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;

    @SerializedName("user_id")
    @Expose
    @Column(name="user_id")
    private Integer userId;

    @SerializedName("merchant_id")
    @Expose
    @Column(name="merchant_id")
    private Integer merchantId;

    @SerializedName("optometrist_id")
    @Expose
    @Column(name="optometrist_id")
    private Integer optometristId;

    @SerializedName("recept_type")
    @Expose
    @Column(name="recept_type")
    private Integer receptType;

    @SerializedName("recept_step")
    @Expose
    @Column(name="recept_step")
    private Integer receptStep;

    @SerializedName("recept_detail_step")
    @Expose
    @Column(name="recept_detail_step")
    private Integer receptDetailStep;

    @SerializedName("recept_detail_step_param")
    @Expose
    @Column(name="recept_detail_step_param")
    private Integer receptDetailStepParam;

    @SerializedName("recept_detail_step_status")
    @Expose
    @Column(name="recept_detail_step_status")
    private Integer receptDetailStepStatus;

    @SerializedName("device_id")
    @Expose
    @Column(name="device_id")
    private Integer deviceId;

    @SerializedName("result_id")
    @Expose
    @Column(name="result_id")
    private Integer resultId;

    @SerializedName("recept_total_date")
    @Expose
    @Column(name="recept_total_date")
    private Integer receptTotalDate;

    @SerializedName("recept_start_date")
    @Expose
    @Column(name="recept_start_date")
    private Integer receptStartDate;

    @SerializedName("recept_end_date")
    @Expose
    @Column(name="recept_end_date")
    private Integer receptEndDate;

    @SerializedName("created_at")
    @Expose
    @Column(name="created_at")
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    @Column(name="updated_at")
    private String updatedAt;

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
     * The merchantId
     */
    public Integer getMerchantId() {
        return merchantId;
    }

    /**
     *
     * @param merchantId
     * The merchant_id
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
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
     * The receptType
     */
    public Integer getReceptType() {
        return receptType;
    }

    /**
     *
     * @param receptType
     * The recept_type
     */
    public void setReceptType(Integer receptType) {
        this.receptType = receptType;
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
     *
     * @return
     * The deviceId
     */
    public Integer getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The device_id
     */
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     * The resultId
     */
    public Integer getResultId() {
        return resultId;
    }

    /**
     *
     * @param resultId
     * The result_id
     */
    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    /**
     *
     * @return
     * The receptTotalDate
     */
    public Integer getReceptTotalDate() {
        return receptTotalDate;
    }

    /**
     *
     * @param receptTotalDate
     * The recept_total_date
     */
    public void setReceptTotalDate(Integer receptTotalDate) {
        this.receptTotalDate = receptTotalDate;
    }

    /**
     *
     * @return
     * The receptStartDate
     */
    public Integer getReceptStartDate() {
        return receptStartDate;
    }

    /**
     *
     * @param receptStartDate
     * The recept_start_date
     */
    public void setReceptStartDate(Integer receptStartDate) {
        this.receptStartDate = receptStartDate;
    }

    /**
     *
     * @return
     * The receptEndDate
     */
    public Integer getReceptEndDate() {
        return receptEndDate;
    }

    /**
     *
     * @param receptEndDate
     * The recept_end_date
     */
    public void setReceptEndDate(Integer receptEndDate) {
        this.receptEndDate = receptEndDate;
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
}
