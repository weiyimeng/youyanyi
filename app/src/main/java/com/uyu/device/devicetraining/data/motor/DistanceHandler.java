package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/2/3.
 */
public class DistanceHandler {
    //单位mm
    /**
     * 屏原点距离眼睛距离
     */
    //老的socket的距离
//    public static int ScreenOriginDistance = 530;
    public static int ScreenOriginDistance = 534;
    /**
     * 裂隙尺挡板原点距离眼睛距离
     */
    //老的socket的距离
//    public static int FracturedRulerBaffleOriginDistance = 106;
    public static int FracturedRulerBaffleOriginDistance = 98;
    /**
     * 推进针原点距离眼睛距离
     */
    //老的socket的距离
//    public static int ApproachNeedleOriginDistance = 42;
    public static int ApproachNeedleOriginDistance = 59;

    /**
     * 计算电机屏的距离
     * @param value
     * @return
     */
    public static int computeMotorScreenDistance(int value){
        int distance = ScreenOriginDistance-value;
        return distance;
    }

    /**
     * 计算点击挡板的距离，在裂隙尺中
     * @param value
     * @return
     */
    public static int computeMotorBaffleDistanceInFracturedRuler(int value){
        int distance = value-FracturedRulerBaffleOriginDistance;
        return distance;
    }

    /**
     * 计算推进针距离眼睛的距离，在推进训练中
     * @param value
     * @return
     */
    public static int computeBaffleRealDistanceInApproach(int value){
        int distance = value+ApproachNeedleOriginDistance;
        return distance;
    }

    /**
     * 计算推进针虚拟的距离
     * @param realValue
     * @return
     */
    public static int computeBaffleVirtualDistanceInApproach(int realValue){
        int distance = realValue-ApproachNeedleOriginDistance;
        return distance;
    }
}
