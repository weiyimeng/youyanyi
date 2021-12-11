package com.uyu.device.devicetraining.data.entity.util;

import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBackDetail;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.presentation.model.VisionCardLevel;
import com.uyu.device.devicetraining.presentation.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2016/6/3.
 */
public class TrialScoreManager {
    /**
     * 计算翻转拍视疲劳得分
     * @param eyeType
     * @param list
     * @return
     */
    public static int computeReversalTrain(EnumEyeType eyeType, List<ReversalTrainBackDetail> list){
        if (list==null || list.size() == 0)
        {
            return 100;
        }

        float mmSum = 0;
        int countSum = 0;

        for (ReversalTrainBackDetail dtoDetail:list)
        {
            int count = dtoDetail.getPassCount();
            int level = dtoDetail.getLevelId();

            VisionCardLevel visionCardLevel = Util.arrayVisionCardLevels[level];
            mmSum += visionCardLevel.getTextSize()  * count;

            countSum += count;
        }

        float avgmm = 0;
        if(countSum==0){
            avgmm = 100;
        }else{
            int standardCount = getStandardCount(eyeType);
            avgmm = mmSum * standardCount / (countSum * countSum);
        }

        //保留一位小数
        float realavgmm = avgmm;

        List<SizeStandard> listSizeStandard = getAllStandardSize();

        //默认最大，如果超过5.5mm的平均值，则默认就是100
        int Score = 100;
        for (int i = 0; i < listSizeStandard.size(); i++)
        {
            //如果过小，则需要加上偏移量，如果过大，不会有影响
            if (realavgmm <= listSizeStandard.get(i).mmSize + SizeStandard.pianyi)
            {
                Score = listSizeStandard.get(i).score;
                break;
            }
        }

        return Score;
    }

    public static int getStandardCount(EnumEyeType eye_type){
        if(eye_type== EnumEyeType.DOUBLE){
            return 16;
        }else{
            return 22;
        }
    }

    public static List<SizeStandard>  getAllStandardSize(){
        List<SizeStandard> list = new ArrayList<>();
        float mmSize = 1.5f;
        int score = 0;
        list.add(new SizeStandard(mmSize, 1));

        int step = 4;
        while (mmSize <= 1.9 + SizeStandard.pianyi)
        {
            mmSize += 0.1;
            score += step;
            list.add(new SizeStandard(mmSize, score));
        }

        step = 2;
        while (mmSize <= 4.4 + SizeStandard.pianyi)
        {
            mmSize += 0.1;
            score += step;
            list.add(new SizeStandard(mmSize, score));
        }

        step = 3;
        while (mmSize <= 5.4 + SizeStandard.pianyi)
        {
            mmSize += 0.1;
            score += step;
            list.add(new SizeStandard(mmSize, score));
        }

        return list;
    }
}
