package com.uyu.device.devicetraining.data.entity.config;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2016/4/19.
 */
public class FinishDescManager {
    //训练结束提示
    public static final String[] STEREOSCOPE_FINISH = new String[]{AppProvider.getApplication().getString(R.string.train_end_look_distance_rest), AppProvider.getApplication().getString(R.string.take_down_z_shape_daffle)};
    public static final String[] FRACTURED_RULER_FINISH = new String[]{AppProvider.getApplication().getString(R.string.train_end_look_distance_rest), AppProvider.getApplication().getString(R.string.take_down_single_or_double_daffle)};
    public static final String[] APPROACH_FINISH = new String[]{AppProvider.getApplication().getString(R.string.train_end_look_distance_rest), AppProvider.getApplication().getString(R.string.take_down_duide_rod)};
    public static final String[] NORMAL_FINISH = new String[]{AppProvider.getApplication().getString(R.string.train_end_look_distance_rest)};

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
