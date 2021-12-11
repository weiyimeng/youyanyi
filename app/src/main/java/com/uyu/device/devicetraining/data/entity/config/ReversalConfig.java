package com.uyu.device.devicetraining.data.entity.config;

/**
 * Created by windern on 2015/12/11.
 */
public class ReversalConfig {
    /**
     * 转盘默认的时间,现在是转到位了才开始刷新界面开始计时
     */
    public final static int GlassTurnDefultSecond = 0;
    /**
     * 1s的时间
     */
    public final static int OneSecondTime = 1000;
    public final static int TimeRecordIntervalSecond = 1;

    /**
     * 文字延迟显示时间
     */
    public final static int OverlayShowLetterTime = 800;
    public final static int glassMaxSecond = 8;
    /**
     * 一次训练时间-自助
     */
    public final static int oneTrainingSecond = 150;
    public final static int oneTrainingSecondDouble = 60*3;
//    public final static int oneTrainingSecond = 5;

    /**
     * 一次训练时间-控制
     */
    public final static int oneTrainingSecondControl = 60*5;
    public final static int oneTrainingSecondDoubleControl = 60*5;

    /**
     * 寻找最大镜片的时间
     */
    public final static int FindRightGlassMaxSecond = 2*60;

    /**
     * 最大镜片等级
     */
    public final static int MaxGlassLevel = 6;
//    public final static int MaxGlassLevel = 2;

    //镜片等级
    //0:-1.50,1:-2.50,2:-3.50,3:-4.50,4:-6.00,5:-8.00
    //0:+1.50,1:+2.50,2: +2.50,3: +2.50,4:+2.50,5: +2.50
    /**
     * 等级尝试的次数
     */
    public final static int[] arrayLevelTryCount = {5,3,3,3,3,3};
//    public final static int[] arrayLevelTryCount = {1,1,1,1,1,1};

    /**
     * 等级得分区间
     */
    public final static int[] arrayLevelSectionScore = {40,52,64,76,88,100};

    /**
     * 变小的时间-单眼
     */
    public final static int SmallOverTime = 3*1000;
    /**
     * 不变的时间-单眼
     */
    public final static int NoChangeOverTime = 5*1000;
    /**
     * 变大的时间-单眼
     */
    public final static int BigOverTime = 8*1000;
    /**
     * 变小的时间-单眼
     */
    public final static int SmallOverTimeDouble = 3*1000;
    /**
     * 不变的时间-单眼
     */
    public final static int NoChangeOverTimeDouble = 5*1000;
    /**
     * 变大的时间-单眼
     */
    public final static int BigOverTimeDouble = 8*1000;
    /**
     * 默认文字大小
     */
    public final static int DefaultLetterSize = 13;

    /**
     * 缓存的数量-针对数字和字母的
     */
    public final static int MaxCacheLetterCount = 500;

    /**
     * 获取等级尝试比较的次数
     * @param glassLevel
     * @return
     */
    public static int getLevelTryCompareCount(int glassLevel){
        int levelTryCompareCount = 0;
        for(int i=0;i<=glassLevel;i++){
            levelTryCompareCount += arrayLevelTryCount[glassLevel];
        }
        return levelTryCompareCount;
    }
}
