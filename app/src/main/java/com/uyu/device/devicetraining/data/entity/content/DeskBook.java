package com.uyu.device.devicetraining.data.entity.content;

import com.uyu.device.devicetraining.data.entity.type.EnumContentType;

/**
 * Created by windern on 2016/5/27.
 */
public class DeskBook {
    /**
     * 类型是 新闻分类
     */
    public static final int CATEGORY_NEWS_CLASSIFY = -2;
    /**
     * 类型是 单个新闻
     */
    public static final int CATEGORY_SINGLE_NEWS = -1;

    /**
     * 类型是 wifi传书
     */
    public static final int CATEGORY_WIFI_BOOK = 1;
    /**
     * 类型是 自定义图书
     */
    public static final int CATEGORY_DIY_BOOK = 2;
    /**
     * 类型是 云图书
     */
    public static final int CATEGORY_CLOUD_BOOK = 3;

    /**
     * 书架中显示
     */
    public static final int VISIBLE = 1;
    /**
     * 书架中不显示
     */
    public static final int INVISIBLE = 0;

    private int id;
    private String title;
    private String picUrl;

    private EnumContentType contentType = EnumContentType.ARTICLE;

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

    public EnumContentType getContentType() {
        return contentType;
    }

    public void setContentType(EnumContentType contentType) {
        this.contentType = contentType;
    }

    public static DeskBook getNumberDeskBook(){
        DeskBook deskBook = new DeskBook();
        deskBook.setContentType(EnumContentType.NUMBER);
        deskBook.setId(0);
        deskBook.setTitle("数字");
        deskBook.setPicUrl("");
        return deskBook;
    }

    public static DeskBook getLetterDeskBook(){
        DeskBook deskBook = new DeskBook();
        deskBook.setContentType(EnumContentType.LETTER);
        deskBook.setId(0);
        deskBook.setTitle("字母");
        deskBook.setPicUrl("");
        return deskBook;
    }
}
