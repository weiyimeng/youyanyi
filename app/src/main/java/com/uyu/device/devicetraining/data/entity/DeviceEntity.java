package com.uyu.device.devicetraining.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2015/11/30.
 */
public class DeviceEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("product_uid")
    @Expose
    private String product_uid;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("merchant_id")
    @Expose
    private int merchant_id;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("usable_state")
    @Expose
    private int usable_state;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("code")
    @Expose
    private String code;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setProduct_uid(String product_uid){
        this.product_uid = product_uid;
    }
    public String getProduct_uid(){
        return this.product_uid;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setMerchant_id(int merchant_id){
        this.merchant_id = merchant_id;
    }
    public int getMerchant_id(){
        return this.merchant_id;
    }
    public void setAlias(String alias){
        this.alias = alias;
    }
    public String getAlias(){
        return this.alias;
    }
    public void setUsable_state(int usable_state){
        this.usable_state = usable_state;
    }
    public int getUsable_state(){
        return this.usable_state;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }
    public String getUpdated_at(){
        return this.updated_at;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
