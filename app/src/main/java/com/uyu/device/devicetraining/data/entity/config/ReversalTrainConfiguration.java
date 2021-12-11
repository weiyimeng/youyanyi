package com.uyu.device.devicetraining.data.entity.config;

/**
 * Created by windern on 2015/12/12.
 */
public class ReversalTrainConfiguration {
    /**
     * 一次训练时间
     */
    public int oneTrainingSecond = ReversalConfig.oneTrainingSecond;

    private ReversalTrainConfiguration(Builder builder){
        this.oneTrainingSecond = builder.oneTrainingSecond;
    }

    public static class Builder{
        private int oneTrainingSecond = ReversalConfig.oneTrainingSecond;
        private int glassMaxSecond = ReversalConfig.glassMaxSecond;

        public Builder setOneTrainingSecond(int oneTrainingSecond) {
            this.oneTrainingSecond = oneTrainingSecond;
            return this;
        }

        public ReversalTrainConfiguration build(){
            return new ReversalTrainConfiguration(this);
        }
    }
}
