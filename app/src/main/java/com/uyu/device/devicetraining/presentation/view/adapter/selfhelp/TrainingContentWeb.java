package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2016/1/8.
 */
public class TrainingContentWeb {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_id")
    @Expose
    private int category_id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("brief")
    @Expose
    public String brief;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("is_visible")
    @Expose
    private int is_visible;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("download_amount")
    @Expose
    public int download_amount;
    @SerializedName("pic_url")
    @Expose
    public String pic_url;
    @SerializedName("create_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("user_nick_name")
    @Expose
    public String user_nick_name;
    @SerializedName("category_name")
    @Expose
    public String category_name;

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIs_visible(int is_visible) {
        this.is_visible = is_visible;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDownload_amount(int download_amount) {
        this.download_amount = download_amount;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public String getContent() {
        return content;
    }

    public int getIs_visible() {
        return is_visible;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDownload_amount() {
        return download_amount;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public void setUser_nick_name(String user_nick_name) {
        this.user_nick_name = user_nick_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
