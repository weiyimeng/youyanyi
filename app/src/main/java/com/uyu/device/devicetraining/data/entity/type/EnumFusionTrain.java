package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/5.
 */
public enum EnumFusionTrain {
    BO(0,"BO", AppProvider.getApplication().getString(R.string.aggregate)), BI(1,"BI",AppProvider.getApplication().getString(R.string.spread));

    /**
     * 值
     */
    private int value;

    /**
     * 名称
     */
    private String name;
    /**
     * 显示名称
     */
    private String showName;

    /*
     * 构造方法
     */
    private EnumFusionTrain(int value,String name,String showName) {
        this.value = value;
        this.name = name;
        this.showName = showName;
    }

    /**
     * 获取值
     * @return
     */
    public int getValue(){
        return value;
    }

    /**
     * 获取名称
     * @return
     */
    public String getName(){
        return name;
    }

    public String getShowName() {
        return showName;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
