package com.uyu.device.devicetraining.data.entity.other;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/3/15.
 */
public class ReceptionTrial {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("merchant_id")
    @Expose
    private int merchantId;
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The merchantId
     */
    public int getMerchantId() {
        return merchantId;
    }

    /**
     *
     * @param merchantId
     * The merchant_id
     */
    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    /**
     *
     * @return
     * The deviceId
     */
    public int getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The device_id
     */
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     * The resultId
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result_id
     */
    public void setResult(String result) {
        this.result = result;
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
