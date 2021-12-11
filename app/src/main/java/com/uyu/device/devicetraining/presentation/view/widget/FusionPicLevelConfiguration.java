package com.uyu.device.devicetraining.presentation.view.widget;

import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;

/**
 * Created by windern on 2015/12/12.
 */
public class FusionPicLevelConfiguration {
    public int failMaxTime = StereoscopeConfig.Fail_Max_Time;
    public int fusingTime = StereoscopeConfig.Fusing_Max_Time;
    public int keepingTime = StereoscopeConfig.Keeping_Max_Time;
    public String fusingTip = StereoscopeConfig.Fusing_Tip;
    public String keepingTip = "";
    public String finishTip = StereoscopeConfig.Finish_Tip;
    public int fusingTipResid = StereoscopeConfig.Fusing_Tip_Resid;
    public int keepingTipResid = StereoscopeConfig.Keeping_Tip_Resid;
    public int finishTipResid = 0;
    /**
     * toast的位置，0上部，1下部
     */
    public int toastPosition = 0;

    private FusionPicLevelConfiguration(Builder builder){
        this.failMaxTime = builder.failMaxTime;
        this.fusingTime = builder.fusingTime;
        this.keepingTime = builder.keepingTime;
        this.fusingTip = builder.fusingTip;
        this.keepingTip = builder.keepingTip;
        this.finishTip = builder.finishTip;
        this.fusingTipResid = builder.fusingTipResid;
        this.keepingTipResid = builder.keepingTipResid;
        this.finishTipResid = builder.finishTipResid;
        this.toastPosition = builder.toastPosition;
    }

    public static class Builder{
        private int failMaxTime = StereoscopeConfig.Fail_Max_Time;
        private int fusingTime = StereoscopeConfig.Fusing_Max_Time;
        private int keepingTime = StereoscopeConfig.Keeping_Max_Time;
        private String fusingTip = StereoscopeConfig.Fusing_Tip;
        private String keepingTip = StereoscopeConfig.Keeping_Tip;
        private String finishTip = "";
        public int fusingTipResid = StereoscopeConfig.Fusing_Tip_Resid;
        public int keepingTipResid = StereoscopeConfig.Keeping_Tip_Resid;
        public int finishTipResid = 0;
        public int toastPosition = 0;

        public Builder setFailMaxTime(int failMaxTime) {
            this.failMaxTime = failMaxTime;
            return this;
        }

        public Builder setFusingTime(int fusingTime) {
            this.fusingTime = fusingTime;
            return this;
        }

        public Builder setKeepingTime(int keepingTime) {
            this.keepingTime = keepingTime;
            return this;
        }

        public Builder setFusingTip(String fusingTip) {
            this.fusingTip = fusingTip;
            return this;
        }

        public Builder setKeepingTip(String keepingTip) {
            this.keepingTip = keepingTip;
            return this;
        }

        public Builder setFinishTip(String finishTip) {
            this.finishTip = finishTip;
            return this;
        }

        public Builder setFusingTipResid(int fusingTipResid) {
            this.fusingTipResid = fusingTipResid;
            return this;
        }

        public Builder setKeepingTipResid(int keepingTipResid) {
            this.keepingTipResid = keepingTipResid;
            return this;
        }

        public Builder setFinishTipResid(int finishTipResid) {
            this.finishTipResid = finishTipResid;
            return this;
        }

        public Builder setToastPosition(int toastPosition) {
            this.toastPosition = toastPosition;
            return this;
        }

        public FusionPicLevelConfiguration build(){
            return new FusionPicLevelConfiguration(this);
        }
    }
}
