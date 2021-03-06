package com.uyu.device.devicetraining.presentation.internal.di.modules;

import android.content.Context;

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
import com.uyu.device.devicetraining.presentation.AndroidApplication;
import com.uyu.device.devicetraining.presentation.navigation.Navigator;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by windern on 2015/11/28.
 */
@Module
public class ApplicationModule {
    private final AndroidApplication application;

    public
    ApplicationModule(AndroidApplication application){
        this.application = application;
    }

    @Provides @Singleton
    Context provideContext(){
        return this.application;
    }

    @Provides @Singleton
    Navigator provideNavigator(){
        return new Navigator();
    }

    @Provides @Singleton
    ApiService provideApiService(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EnumFusionTrain.class, new EnumFusionTrainSerializer())
                .registerTypeAdapter(EnumPresAdjust.class, new EnumPresAdjustSerializer())
                .registerTypeAdapter(EnumEyeType.class, new EnumEyeTypeSerializer())
                .registerTypeAdapter(EnumGlassType.class, new EnumGlassTypeSerializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceConfig.API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // ??????Rx?????????
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    @Provides @Singleton
    TtsEngine provideTtsEngine(){
        TtsEngine ttsEngine = new TtsEngine(application);
        return ttsEngine;
    }
}
