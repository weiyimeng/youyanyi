package com.uyu.device.devicetraining.data.entity.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.uyu.device.devicetraining.data.db.AppDatabase;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumShowContentType;

/**
 * Created by windern on 2016/5/25.
 */
@Table(database = AppDatabase.class)
public class News extends BaseModel {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public int id;

    @SerializedName("source_id")
    @Expose
    @Column(name="source_id")
    public int sourceId;

    @SerializedName("category_id")
    @Expose
    @Column(name="category_id")
    public int categoryId;

    @SerializedName("news_title")
    @Expose
    @Column(name="news_title")
    public String newsTitle;

    @SerializedName("news_brief")
    @Expose
    @Column(name="news_brief")
    public String newsBrief;

    @SerializedName("news_link")
    @Expose
    @Column(name="news_link")
    public String newsLink;

    @SerializedName("news_content")
    @Expose
    @Column(name="news_content")
    public String newsContent;

    @SerializedName("news_date")
    @Expose
    @Column(name="news_date")
    public int newsDate;

    @SerializedName("created_at")
    @Expose
    @Column(name="created_at")
    public String createdAt;

    @SerializedName("updated_at")
    @Expose
    @Column(name="updated_at")
    public String updatedAt;

    @SerializedName("news_clicks")
    @Expose
    @Column(name="news_clicks")
    public int newsClicks;

    /**
     * 返回一个ShowContent的实例
     * @return
     */
    public ShowContent toShowContent(){
        ShowContent showContent = new ShowContent();
        showContent.setTitle(newsTitle);
        showContent.setContent(ContentManager.removeEmptyChar(newsContent));
        showContent.setShowContentType(EnumShowContentType.NORMAL);
        showContent.setTrainingContentType(EnumContentType.NEWS);
        showContent.setTrainingContentCategoryId(categoryId);
        showContent.setTrainingContentArticleId(id);
        return showContent;
    }
}