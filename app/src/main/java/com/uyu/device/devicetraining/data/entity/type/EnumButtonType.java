package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;

/**
 * Created by windern on 2016/1/7.
 */
public enum EnumButtonType {
    FINISH_ALL(0,"结束全部", R.string.finish_train),
    FINISH_ITEM(1,"结束小项", R.string.finish_item_train),
    REFRESH_CODE(2,"刷新二维码", R.string.refresh_code);

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;
    /**
     * 字符
     */
    private int tipid;

    /*
     * 构造方法
     */
    private EnumButtonType(int value, String name, int tipid) {
        this.value = value;
        this.name = name;
        this.tipid = tipid;
    }

    /**
     * 获取名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    public int getTipid() {
        return tipid;
    }
}
