package com.uyu.device.devicetraining.domain.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uyu.device.devicetraining.data.entity.adapter.EnumEyeTypeSerializer;
import com.uyu.device.devicetraining.data.entity.adapter.EnumFusionTrainSerializer;
import com.uyu.device.devicetraining.data.entity.adapter.EnumGlassTypeSerializer;
import com.uyu.device.devicetraining.data.entity.adapter.EnumPresAdjustSerializer;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;
import com.uyu.device.devicetraining.data.entity.type.EnumPresAdjust;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by windern on 2016/4/5.
 */
public class ServiceFactory {
    public static ApiService createApiServie(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EnumFusionTrain.class, new EnumFusionTrainSerializer())
                .registerTypeAdapter(EnumPresAdjust.class, new EnumPresAdjustSerializer())
                .registerTypeAdapter(EnumEyeType.class, new EnumEyeTypeSerializer())
                .registerTypeAdapter(EnumGlassType.class, new EnumGlassTypeSerializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceConfig.API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }
}
