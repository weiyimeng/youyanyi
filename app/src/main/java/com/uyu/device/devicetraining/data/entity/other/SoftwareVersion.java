package com.uyu.device.devicetraining.data.entity.other;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/5/12.
 */
public class SoftwareVersion {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("version_code")
    @Expose
    private int versionCode;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("mark")
    @Expose
    private String mark;
    @SerializedName("path")
    @Expose
    private String path;
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
     * The versionCode
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     *
     * @param versionCode
     * The version_code
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     *
     * @return
     * The versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     *
     * @param versionName
     * The version_name
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     *
     * @return
     * The mark
     */
    public String getMark() {
        return mark;
    }

    /**
     *
     * @param mark
     * The mark
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     *
     * @return
     * The path
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     * The path
     */
    public void setPath(String path) {
        this.path = path;
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
