package com.uyu.device.devicetraining.data.entity.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.uyu.device.devicetraining.data.db.AppDatabase;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;

/**
 * Created by windern on 2016/5/25.
 */
@Table(database = AppDatabase.class)
public class NewsCategory extends BaseModel {
    /**
     * 分类id
     */
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public int id;

    /**
     * 分类名称
     */
    @SerializedName("category_name")
    @Expose
    @Column(name="category_name")
    public String categoryName;

    /**
     * 分类图片地址
     */
    @SerializedName("category_pic")
    @Expose
    @Column(name="category_pic")
    public String categoryPic;

    @SerializedName("created_at")
    @Expose
    @Column(name="created_at")
    public String createdAt;

    @SerializedName("updated_at")
    @Expose
    @Column(name="updated_at")
    public String updatedAt;

    /**
     * 返回一个DeskBook显示的实例
     * @return
     */
    public DeskBook toDeskBook(){
        DeskBook deskBook = new DeskBook();
        deskBook.setContentType(EnumContentType.NEWS);
        deskBook.setId(id);
        deskBook.setTitle(categoryName);
        deskBook.setPicUrl(categoryPic);
        return deskBook;
    }
}
