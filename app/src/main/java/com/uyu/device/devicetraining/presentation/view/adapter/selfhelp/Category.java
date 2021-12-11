package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/1/8.
 */
public class Category {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("parent_id")
    @Expose
    public int parent_id;
    @SerializedName("pic_url")
    @Expose
    public String pic_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Category clone() {
        Category category = null;
        category = new Category();
        category.id = this.id;
        category.name = this.name;
        category.parent_id = this.parent_id;
        category.pic_url = this.pic_url;
        return category;
    }

}
