package com.uyu.device.devicetraining.data.entity.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by jwc on 2016/8/15.
 */
public class VersionEmqttMessageContent {


    @SerializedName("software_version_url")
    @Expose
    private String softwareVersionUrl;

    @SerializedName("software_version_code")
    @Expose
    private String softwareVersionCode;

    @SerializedName("hardware_version__url")
    @Expose
    private String hardwareVersionUrl;

    @SerializedName("hardware_version_code")
    @Expose
    private String hardwareVersionCode;

    public static VersionEmqttMessageContent convert(JSONObject jsonObject){
        try {
            VersionEmqttMessageContent versionEmqttMessageContent = new VersionEmqttMessageContent();
            versionEmqttMessageContent.softwareVersionUrl = jsonObject.getString("software_version_url");
            versionEmqttMessageContent.softwareVersionCode = jsonObject.getString("software_version_code");
            versionEmqttMessageContent.hardwareVersionUrl = jsonObject.getString("hardware_version__url");
            versionEmqttMessageContent.hardwareVersionCode = jsonObject.getString("hardware_version_code");
            return versionEmqttMessageContent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSoftwareVersionUrl() {
        return softwareVersionUrl;
    }

    public String getSoftwareVersionCode() {
        return softwareVersionCode;
    }

    public String getHardwareVersionUrl() {
        return hardwareVersionUrl;
    }

    public String getHardwareVersionCode() {
        return hardwareVersionCode;
    }
}
