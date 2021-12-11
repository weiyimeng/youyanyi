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
 * Created by windern on 2015/12/9.
 */
@Table(database = AppDatabase.class)
public class TrainingContent extends BaseModel {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private int id;
    @SerializedName("category_id")
    @Expose
    @Column(name="category_id")
    private int categoryId;
    @SerializedName("title")
    @Expose
    @Column(name="title")
    private String title;
    @SerializedName("breif")
    @Expose
    @Column(name="breif")
    private String breif;
    @SerializedName("content")
    @Expose
    @Column(name="content")
    private String content;
    @SerializedName("is_visible")
    @Expose
    @Column(name="is_visible")
    private int isVisible;
    @SerializedName("user_id")
    @Expose
    @Column(name="user_id")
    private int userId;
    @SerializedName("download_amount")
    @Expose
    @Column(name="download_amount")
    private int downloadAmount;
    @SerializedName("pic_url")
    @Expose
    @Column(name="pic_url")
    private String picUrl;
    @SerializedName("created_at")
    @Expose
    @Column(name="created_at")
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    @Column(name="updated_at")
    private String updatedAt;
    @SerializedName("seq_show")
    @Expose
    @Column(name="seq_show")
    private int seqShow = 0;
    @SerializedName("type")
    @Expose
    @Column(name="type")
    private int type = 0;

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
     * The categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @param categoryId
     * The category_id
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The breif
     */
    public String getBreif() {
        return breif;
    }

    /**
     *
     * @param breif
     * The breif
     */
    public void setBreif(String breif) {
        this.breif = breif;
    }

    /**
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     * The isVisible
     */
    public int getIsVisible() {
        return isVisible;
    }

    /**
     *
     * @param isVisible
     * The is_visible
     */
    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
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
     * The downloadAmount
     */
    public int getDownloadAmount() {
        return downloadAmount;
    }

    /**
     *
     * @param downloadAmount
     * The download_amount
     */
    public void setDownloadAmount(int downloadAmount) {
        this.downloadAmount = downloadAmount;
    }

    /**
     *
     * @return
     * The picUrl
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     *
     * @param picUrl
     * The pic_url
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    /**
     *
     * @return
     * The seqShow
     */
    public int getSeqShow() {
        return seqShow;
    }

    /**
     *
     * @param seqShow
     * The seq_show
     */
    public void setSeqShow(int seqShow) {
        this.seqShow = seqShow;
    }

    /**
     *
     * @return
     * The type
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 返回一个DeskBook显示的实例
     * @return
     */
    public DeskBook toDeskBook(){
        DeskBook deskBook = new DeskBook();
        deskBook.setContentType(EnumContentType.ARTICLE);
        deskBook.setId(id);
        deskBook.setTitle(title);
        deskBook.setPicUrl(picUrl);
        return deskBook;
    }

    /**
     * 返回一个ShowContent的实例
     * @return
     */
    public ShowContent toShowContent(){
        ShowContent showContent = new ShowContent();
        showContent.setTitle(title);
        showContent.setContent(ContentManager.removeEmptyChar(content));
        showContent.setShowContentType(EnumShowContentType.values()[type]);
        showContent.setTrainingContentType(EnumContentType.ARTICLE);
        showContent.setTrainingContentCategoryId(categoryId);
        showContent.setTrainingContentArticleId(id);
        return showContent;
    }
}
