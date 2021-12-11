package com.uyu.device.devicetraining.presentation.type;

/**
 * Created by windern on 2015/12/11.
 */
public enum EnumFusionTimeType {
    FUSING(0,"融合中","将左右两幅图合成一副保持住，同时按下确认键"),
    KEEPING(1,"保持中","请继续保持，直至图像换成其他内容，如果不能保持，请再次按下确认键"),
    FINISH(2,"结束","结束了");

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;
    /**
     * 提示
     */
    private String tip;

    /*
     * 构造方法
     */
    private EnumFusionTimeType(int value,String name,String tip) {
        this.value = value;
        this.name = name;
        this.tip = tip;
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

    public String getTip() {
        return tip;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
