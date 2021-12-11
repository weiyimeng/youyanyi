package com.uyu.device.devicetraining.presentation.view.widget;

import com.uyu.device.devicetraining.data.entity.config.RGVariableVectorConfig;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;

/**
 * Created by windern on 2015/12/12.
 */
public class RGVariableConfiguration {
    public int extraEndWaitSecond = RGVariableVectorConfig.ExtraEndWaitSecond;
    public int twoSplitMaxSecond = RGVariableVectorConfig.TwoSplitMaxSecond;


    private RGVariableConfiguration(Builder builder){
        this.extraEndWaitSecond = builder.extraEndWaitSecond;
        this.twoSplitMaxSecond = builder.twoSplitMaxSecond;
    }

    public static class Builder{
        private int extraEndWaitSecond = RGVariableVectorConfig.ExtraEndWaitSecond;
        private int twoSplitMaxSecond = RGVariableVectorConfig.TwoSplitMaxSecond;

        public Builder setExtraEndWaitSecond(int extraEndWaitSecond) {
            this.extraEndWaitSecond = extraEndWaitSecond;
            return this;
        }

        public Builder setTwoSplitMaxSecond(int twoSplitMaxSecond) {
            this.twoSplitMaxSecond = twoSplitMaxSecond;
            return this;
        }

        public RGVariableConfiguration build(){
            return new RGVariableConfiguration(this);
        }
    }
}
