package com.uyu.device.devicetraining.data.entity.config;

/**
 * Created by windern on 2016/6/16.
 */
public class ReversalTrainConfigurationFactory {
    public static ReversalTrainConfiguration create(){
        ReversalTrainConfiguration configuration = new ReversalTrainConfiguration.Builder()
                .setOneTrainingSecond(ReversalConfig.oneTrainingSecond)
                .build();
        return configuration;
    }
}
