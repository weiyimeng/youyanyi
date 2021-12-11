package com.uyu.device.devicetraining.data.net.api;

import com.uyu.device.devicetraining.data.net.api.retrofit.JsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/3/25 0025.
 */
public class ServiceFactory {
    public static final int TYPE_STRING = 0;
    public static final int TYPE_OBJECT = 1;

    private ServiceFactory() {
    }

    public synchronized static ApiService create(int type) {
        Retrofit retrofit = null;
        switch (type) {
            case TYPE_OBJECT:
                retrofit = new Retrofit.Builder()
                        .baseUrl(ServiceConfig.API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                break;
            case TYPE_STRING:
                //是通过JsonConverterFactory创建的，不走gson，直接返回string的原封数据
                retrofit = new Retrofit.Builder()
                        .baseUrl(ServiceConfig.API_URL)
                        .addConverterFactory(JsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                break;
            default:
                retrofit = new Retrofit.Builder()
                        .baseUrl(ServiceConfig.API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                break;
        }
        return retrofit.create(ApiService.class);
    }
}
