package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.adapter.OnFailListener;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.type.EnumFusionTimeType;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/11.
 */
public class FusionPicLevel extends RelativeLayout {
    private Context context;
    private View view;

    @Bind(R.id.fusion_pic_container)
    RelativeLayout fusion_pic_container;
    @Bind(R.id.fusion_pic_left)
    ImageView fusionPicLeft;
    @Bind(R.id.fusion_pic_right)
    ImageView fusionPicRight;

    private float intervalSpaceMM = 88;
    private int leftPicResId = R.drawable.stereoscope_bo_left_default;
    private int rightPicResId = R.drawable.stereoscope_bo_right_default;

    public FusionPicLevel(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public FusionPicLevel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public FusionPicLevel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_fusion_pic_view, this,
                true);
        ButterKnife.bind(this, view);

        refreshPic();
    }

    /**
     * 设置相关参数
     *
     * @param intervalSpaceMM 图片间距
     * @param leftPicResId    左边图片
     * @param rightPicResId   右边图片
     */
    public void setParamas(float intervalSpaceMM, int leftPicResId, int rightPicResId) {
        this.intervalSpaceMM = intervalSpaceMM;
        this.leftPicResId = leftPicResId;
        this.rightPicResId = rightPicResId;
        refreshPic();
    }

    /**
     * 刷新图片的显示
     */
    private void refreshPic() {
        int px = Math.round(Util.convertpx(intervalSpaceMM, context));
        //直接marginleft右边的图片，直接确定间距，然后自动就居中
        int margin = px;
//        int margin = (930-px-300)/2;

//        RelativeLayout.LayoutParams paramLeft = (RelativeLayout.LayoutParams) fusionPicLeft.getLayoutParams();
//        paramLeft.leftMargin = margin;
//        fusionPicLeft.setLayoutParams(paramLeft);

        LayoutParams paramRight = (LayoutParams) fusionPicRight.getLayoutParams();
        paramRight.leftMargin = margin;
        fusionPicRight.setLayoutParams(paramRight);

        fusionPicLeft.setImageResource(leftPicResId);
        fusionPicRight.setImageResource(rightPicResId);
    }

    private EnumFusionTimeType fusionTimeType;

    protected int failTime = 0;

    protected OnFinishResultListener resultListener;
    protected OnFailListener failListener;
    protected FusionPicLevelConfiguration configuration;

    protected TtsEngine ttsEngine;

    public void setResultListener(OnFinishResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setFailListener(OnFailListener failListener) {
        this.failListener = failListener;
    }

    public void setConfiguration(FusionPicLevelConfiguration configuration) {
        this.configuration = configuration;
    }

    public FusionPicLevelConfiguration getConfiguration() {
        return configuration;
    }

    public EnumFusionTimeType getFusionTimeType() {
        return fusionTimeType;
    }

    protected Handler fusingTimeHandler = new Handler();
    protected Runnable fusingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            resultListener.onFinishResult(EnumResultType.FAIL);
        }
    };

    protected void startFusingTime() {
        ttsEngine.startSpeaking(configuration.fusingTipResid);
        fusingTimeHandler.postDelayed(fusingTimeRunnable,
                configuration.fusingTime);
    }

    private void stopFusingTime() {
        ttsEngine.stopSpeaking();
        fusingTimeHandler.removeCallbacks(fusingTimeRunnable);
    }

    protected Handler keepingTimeHandler = new Handler();
    protected Runnable keepingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            finishResult(EnumResultType.SUCCESS);
        }
    };

    protected void startKeepingTime() {
        //ttsEngine.startSpeaking(configuration.keepingTipResid);
        int px = Math.round(Util.convertpx(intervalSpaceMM, context));
        int xOffset = px/2;
        if(configuration.toastPosition==0){
            ViewToast.getInstance((configuration.keepingTime / 1000) - 1).showTime(context, Gravity.TOP, xOffset, 190);
        }else {
            ViewToast.getInstance((configuration.keepingTime / 1000) - 1).showTime(context, Gravity.BOTTOM, xOffset, 100);
        }
        keepingTimeHandler.postDelayed(keepingTimeRunnable, configuration.keepingTime);
    }

    private void stopKeepingTime() {
        ttsEngine.stopSpeaking();
        ViewToast.getInstance().removeTime();
        keepingTimeHandler.removeCallbacks(keepingTimeRunnable);
    }

    /**
     * 获取失败次数
     *
     * @return
     */
    public int getFailTime() {
        return failTime;
    }

    /**
     * 恢复到初始状态
     */
    private void resetInitStatus() {
        if (ttsEngine == null) {
            ttsEngine = new TtsEngine(getContext());
        }
        stopFusingTime();
        stopKeepingTime();
        fusionTimeType = EnumFusionTimeType.FUSING;
    }

    /**
     * 开始此关
     */
    public void startLevel() {
        failTime = 0;
        restart();
    }

    /**
     * 重新开始
     */
    private void restart() {
        resetInitStatus();
        startFusingTime();
    }

    /**
     * 确认
     */
    public void confirm() {
        Timber.d("FusionPicLevel confirm:type %s",fusionTimeType.toString());
        if (fusionTimeType == EnumFusionTimeType.FUSING) {
            stopFusingTime();

            startKeepingTime();
            fusionTimeType = EnumFusionTimeType.KEEPING;
        }
        //keeping状态不接收指令，不用管
//        else if (fusionTimeType == EnumFusionTimeType.KEEPING) {
//            stopFusingTime();
//            stopKeepingTime();
//            fusionTimeType = EnumFusionTimeType.FINISH;
//            finishResult(EnumResultType.FAIL);
//        }
    }

    /**
     * 结束
     *
     * @param resultType
     */
    protected void finishResult(EnumResultType resultType) {
        if (resultType == EnumResultType.SUCCESS) {
            if (configuration.finishTipResid != 0) {
                ttsEngine.startSpeaking(configuration.finishTipResid);
            }
            resultListener.onFinishResult(EnumResultType.SUCCESS);
        } else if (resultType == EnumResultType.FAIL) {
            failTime++;
            if (failTime == configuration.failMaxTime) {
                resultListener.onFinishResult(EnumResultType.FAIL);
            } else {
                restart();

                failListener.onFail(failTime);
            }
        }
    }

    /**
     * 结束以后初始化显示的东西
     */
    public void initViewAfterFinish() {
        resetInitStatus();
    }

    public void pauseTrain() {
        resetInitStatus();
    }

    public void resumeTrain() {
        startLevel();
    }
}
