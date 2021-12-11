package com.uyu.device.devicetraining.data.entity.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;

import java.lang.reflect.Type;

/**
 * Created by windern on 2015/12/5.
 */
public class EnumFusionTrainSerializer implements JsonSerializer<EnumFusionTrain>,
        JsonDeserializer<EnumFusionTrain>  {

    // 对象转为Json时调用,实现JsonSerializer<PackageState>接口
    @Override
    public JsonElement serialize(EnumFusionTrain state, Type arg1,
                                 JsonSerializationContext arg2) {
        return new JsonPrimitive(state.getValue());
    }

    // json转为对象时调用,实现JsonDeserializer<PackageState>接口
    @Override
    public EnumFusionTrain deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsInt() < EnumFusionTrain.values().length)
            return EnumFusionTrain.values()[json.getAsInt()];
        return null;
    }

}