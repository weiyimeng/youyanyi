package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;

/**
 * Created by windern on 2016/1/11.
 */
public class RedGreenFusionPicLevel extends FusionPicLevel{
    public RedGreenFusionPicLevel(Context context) {
        super(context);
}

    public RedGreenFusionPicLevel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedGreenFusionPicLevel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置参数
     * @param fusionTrain 融合类型
     * @param intervalSpaceMM 图片间距
     * @param redPicResId 红色图片编号
     * @param redAphla 红色图片透明度
     * @param greenPicResId 绿色图片编号
     * @param greenAphla 绿色图片透明度
     */
    public void setParamas(EnumFusionTrain fusionTrain,float intervalSpaceMM
            ,int redPicResId,float redAphla
            ,int greenPicResId,float greenAphla){
        if(fusionTrain==EnumFusionTrain.BO){
            setParamas(intervalSpaceMM, greenPicResId, redPicResId);
            fusionPicLeft.setAlpha(greenAphla);
            fusionPicRight.setAlpha(redAphla);
        }else{
            setParamas(intervalSpaceMM, redPicResId, greenPicResId);
            fusionPicLeft.setAlpha(redAphla);
            fusionPicRight.setAlpha(greenAphla);
        }
    }
}
