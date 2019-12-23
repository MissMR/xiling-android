package com.xiling.shared.factory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xiling.shared.bean.api.RequestResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/12/14.
 */
public class ResultJsonDeser implements JsonDeserializer<RequestResult<?>> {

    @Override
    public RequestResult<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RequestResult response = new RequestResult();
        try {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                int code = jsonObject.get("code").getAsInt();
                response.code = code;
                response.message = jsonObject.get("message").getAsString();
                if (code != 0) {
                    return response;
                }
                Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                response.data = context.deserialize(jsonObject.get("data"), itemType);
                return response;
            }
        } catch (Exception e) {
            response.code = 2;
            response.message = "服务器数据无法解析";
            e.printStackTrace();
        }
        return response;
    }

}
