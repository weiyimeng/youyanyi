package com.uyu.device.devicetraining.data.entity.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;

import java.lang.reflect.Type;

/**
 * Created by windern on 2015/12/5.
 */
public class EnumEyeTypeSerializer implements JsonSerializer<EnumEyeType>,
        JsonDeserializer<EnumEyeType> {

    // 对象转为Json时调用,实现JsonSerializer<PackageState>接口
    @Override
    public JsonElement serialize(EnumEyeType state, Type arg1,
                                 JsonSerializationContext arg2) {
        return new JsonPrimitive(state.getValue());
    }

    // json转为对象时调用,实现JsonDeserializer<PackageState>接口
    @Override
    public EnumEyeType deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsInt() < EnumEyeType.values().length)
            return EnumEyeType.values()[json.getAsInt()];
        return null;
    }

}