package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/25.
 */
public enum TurntableLocation {
    ZERO_DIOPTER(0,"平镜"),
    FU150(1,"-1.50"),
    ZHENG150(2,"+1.50"),
    FU250(3,"-2.50"),
    ZHENG250_FU250(4,"+2.50属于-2.50"),
    FU350(5,"-3.50"),
    ZHENG250_FU350(6,"+2.50属于-3.50"),
    FU450(7,"-4.50"),
    ZHENG250_FU450(8,"+2.50属于-4.50"),
    FU600(9,"-6.00"),
    ZHENG250_FU600(10,"+2.50属于-6.00"),
    FU800(11,"-8.00"),
    ZHENG250_FU800(12,"+2.50属于-8.00"),
    PRISM(13,"棱镜"),
    RED_GREEN(14,"红绿片"),
    BLANK(15,"挡片");

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;

    /*
     * 构造方法
     */
    private TurntableLocation(int value, String name) {
        this.value = value;
        this.name = name;
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

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TurntableLocation getFuByDegreeLevel(int degreeLevel){
        int value = degreeLevel*2+1;
        TurntableLocation turntableLocation = TurntableLocation.values()[value];
        return turntableLocation;
    }

    public static TurntableLocation getZhengByDegreeLevel(int degreeLevel){
        int value = degreeLevel*2+2;
        TurntableLocation turntableLocation = TurntableLocation.values()[value];
        return turntableLocation;
    }
}
