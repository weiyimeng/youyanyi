package com.uyu.device.devicetraining.data.entity.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.type.EnumContentLoopMode;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumShowContentType;

/**
 * 显示的内容
 * Created by windern on 16/7/8.
 */
public class ShowContent {
    private EnumShowContentType showContentType = EnumShowContentType.NORMAL;
    private String title="";
    private String content="";

    //后加的为trainpres的数据对应上的
    private EnumContentType trainingContentType=EnumContentType.LETTER;
    private Integer trainingContentCategoryId=0;
    private Integer trainingContentArticleId=0;

    public EnumShowContentType getShowContentType() {
        return showContentType;
    }

    public void setShowContentType(EnumShowContentType showContentType) {
        this.showContentType = showContentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EnumContentType getTrainingContentType() {
        return trainingContentType;
    }

    public void setTrainingContentType(EnumContentType trainingContentType) {
        this.trainingContentType = trainingContentType;
    }

    public Integer getTrainingContentCategoryId() {
        return trainingContentCategoryId;
    }

    public void setTrainingContentCategoryId(Integer trainingContentCategoryId) {
        this.trainingContentCategoryId = trainingContentCategoryId;
    }

    public Integer getTrainingContentArticleId() {
        return trainingContentArticleId;
    }

    public void setTrainingContentArticleId(Integer trainingContentArticleId) {
        this.trainingContentArticleId = trainingContentArticleId;
    }
}
