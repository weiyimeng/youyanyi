package com.example;

/**
 * Created by guo on 2017/1/17.
 */
public class Configure {

    private Configure(){

    }

    /**
     * 屏幕长宽（ViewPort）
     */
    public static final float WORLD_WIDTH = 800F;
    public static final float WORLD_HEIGHT = 480F;

    /**
     * 光栅颜色
     */
    public static final int BLACK = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;

    /**
     * 游戏难度
     */
    public static final int EASY_LEVEL = 0;
    public static final int MIDDLE_LEVEL = 1;
    public static final int HIGH_LEVEL = 2;

    /**
     * 各角色的移动速度
     */
    public static final float GUN_SPEED = 40;
    public static final float BALLOON_SPEED = 80;
    public static final float BALL_SPEED = 700;


    /**
     * 气球的时间间隔和距离间隔
     */
    public static final float BALLOON_INTERVAL_TIME = 2.0F;
    public static final float BALLOON_INTERVAL_SPACE = 80;


    /**
     * 枪的起始位置和终止位置
     */
    public static final float GUN_LEFT = 200F;
    public static final float GUN_RIGHT = 600F;


    /**
     * 光栅的宽度 和移动速度
     */
    public static final int GATING_WIDTH_WIDE = 1;
    public static final int GATING_WIDTH_MIDDLE = 2;
    public static final int GATING_WIDTH_FINE = 3;

    public static final float GATING_SPEED_FAST = 18F;
    public static final float GATING_SPEED_MIDDLE = 14.5F;
    public static final float GATING_SPEED_SLOW = 12F;
    public static final float GATING_SPEED_STATIC = 0F;

}
