package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/5.
 */
public enum EnumTrainItem {

    STEREOSCOPE("stereoscope", AppProvider.getApplication().getResources().getString(R.string.title_stereoscope)),
    FRACTURED_RULER("fractured_ruler",AppProvider.getApplication().getResources().getString(R.string.title_fractured_ruler)),
    REVERSAL("reversal",AppProvider.getApplication().getResources().getString(R.string.title_reversal)),
    RED_GREEN_READ("red_green_read",AppProvider.getApplication().getResources().getString(R.string.title_red_green_read)),
    APPROACH("approach",AppProvider.getApplication().getResources().getString(R.string.approach)),
    R_G_VARIABLE_VECTOR("r_g_variable_vector",AppProvider.getApplication().getResources().getString(R.string.title_r_g_variable_vector)),
    R_G_FIXED_VECTOR("r_g_fixed_vector",AppProvider.getApplication().getResources().getString(R.string.title_r_g_fixed_vector)),
    GLANCE("glance",AppProvider.getApplication().getResources().getString(R.string.title_glance)),
    FOLLOW("follow",AppProvider.getApplication().getResources().getString(R.string.title_follow));

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
    private EnumTrainItem(String name, String showName) {
        this.name = name;
        this.showName = showName;
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
        return name;
    }

    public static EnumTrainItem getEnum(String value){
        for(EnumTrainItem re: values()) {
            if (re.name.equals(value)) {
                return re;
            }
        }
        throw new IllegalArgumentException("Invalid RandomEnum value: " + value);
    }
}
