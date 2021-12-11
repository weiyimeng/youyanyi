package com.uyu.device.devicetraining.data.entity.config.trial;

import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;

/**
 * Created by windern on 2016/4/19.
 */
public class FinishDescManager {
    //训练结束提示
    public static final String[] STEREOSCOPE_FINISH = new String[]{"1、训练结束，请望远休息", "2、将Z形分隔板取下"};
    public static final String[] FRACTURED_RULER_FINISH = new String[]{"1、训练结束，请望远休息", "2、请将(单)双孔挡板撤下放好"};
    public static final String[] APPROACH_FINISH = new String[]{"1、训练结束,请望远休息", "2、将引导棒取下放好"};
    public static final String[] NORMAL_FINISH = new String[]{"训练结束,请望远休息"};

    /**
     * 获取结束的提示
     * @param trainPresPre
     * @param trainPresNext
     * @return
     */
    public static String[] getFinishDescList(TrainPres trainPresPre, TrainPres trainPresNext) {
        String[] descList = NORMAL_FINISH;

        if(trainPresPre.isSameInstrument(trainPresNext)){
            //相等就直接休息
            descList = NORMAL_FINISH;
        }else{
            switch (trainPresPre.getTrainItemType()) {
                case STEREOSCOPE:
                    descList = STEREOSCOPE_FINISH;
                    break;
                case FRACTURED_RULER:
                    descList = FRACTURED_RULER_FINISH;
                    break;
                case APPROACH:
                    descList = APPROACH_FINISH;
                    break;
                default:
                    descList = NORMAL_FINISH;
                    break;
            }
        }

        return descList;
    }
}
