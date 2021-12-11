package com.uyu.device.devicetraining.data.net.api.retrofit;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by windern on 2015/12/4.
 */
public class JsonConverterFactory extends Converter.Factory {
    public static JsonConverterFactory create() {
        return new JsonConverterFactory();
    }

    private JsonConverterFactory() {

    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new JsonRequestBodyConverter<>(type);
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsonResponseBodyConverter<>();
    }

}
