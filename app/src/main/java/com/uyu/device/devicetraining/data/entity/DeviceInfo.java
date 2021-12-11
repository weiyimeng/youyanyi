package com.uyu.device.devicetraining.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/5/12.
 */
public class DeviceInfo {
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("software_version_code")
    @Expose
    private int softwareVersionCode;
    @SerializedName("software_version_name")
    @Expose
    private String softwareVersionName;
    @SerializedName("hardware_version_code")
    @Expose
    private int hardwareVersionCode;
    @SerializedName("hardware_version_name")
    @Expose
    private String hardwareVersionName;
    @SerializedName("btid")
    @Expose
    private String btid;
    @SerializedName("cpuid")
    @Expose
    private String cpuid;

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The softwareVersionCode
     */
    public int getSoftwareVersionCode() {
        return softwareVersionCode;
    }

    /**
     *
     * @param softwareVersionCode
     * The software_version_code
     */
    public void setSoftwareVersionCode(int softwareVersionCode) {
        this.softwareVersionCode = softwareVersionCode;
    }

    /**
     *
     * @return
     * The softwareVersionName
     */
    public String getSoftwareVersionName() {
        return softwareVersionName;
    }

    /**
     *
     * @param softwareVersionName
     * The software_version_name
     */
    public void setSoftwareVersionName(String softwareVersionName) {
        this.softwareVersionName = softwareVersionName;
    }

    /**
     *
     * @return
     * The hardwareVersionCode
     */
    public int getHardwareVersionCode() {
        return hardwareVersionCode;
    }

    /**
     *
     * @param hardwareVersionCode
     * The hardware_version_code
     */
    public void setHardwareVersionCode(int hardwareVersionCode) {
        this.hardwareVersionCode = hardwareVersionCode;
    }

    /**
     *
     * @return
     * The hardwareVersionName
     */
    public String getHardwareVersionName() {
        return hardwareVersionName;
    }

    /**
     *
     * @param hardwareVersionName
     * The hardware_version_name
     */
    public void setHardwareVersionName(String hardwareVersionName) {
        this.hardwareVersionName = hardwareVersionName;
    }

    /**
     *
     * @return
     * The btid
     */
    public String getBtid() {
        return btid;
    }

    /**
     *
     * @param btid
     * The btid
     */
    public void setBtid(String btid) {
        this.btid = btid;
    }

    /**
     *
     * @return
     * The cpuid
     */
    public String getCpuid() {
        return cpuid;
    }

    /**
     *
     * @param cpuid
     * The cpuid
     */
    public void setCpuid(String cpuid) {
        this.cpuid = cpuid;
    }
}
