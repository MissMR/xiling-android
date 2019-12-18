package com.xiling.ddmall.shared.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

public class GsonFactory {

    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };

    private static final TypeAdapter<Integer> intAsStringAdapter = new TypeAdapter<Integer>() {

        @Override
        public void write(JsonWriter out, Integer value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Integer read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case NULL:
                    in.nextNull();
                    return 0;
                case NUMBER:
                    return in.nextInt();
                case STRING:
                    try {
                        return Integer.valueOf(in.nextString());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                default:
                    return 0;
            }
        }
    };
    private static final TypeAdapter<Long> longAsStringAdapter = new TypeAdapter<Long>() {

        @Override
        public void write(JsonWriter out, Long value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Long read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case NULL:
                    in.nextNull();
                    return -1L;
                case NUMBER:
                    return in.nextLong();
                case STRING:
                    try {
                        return Long.valueOf(in.nextString());
                    } catch (NumberFormatException e) {
                        return -1L;
                    }
                default:
                    return -1L;
            }
        }
    };

    private static final JsonDeserializer<Date> dateAsStringAdapter = new JsonDeserializer<Date>() {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json != null) {
                final String jsonString = json.getAsString();
                try {
                    return Constants.FORMAT_DATE_FULL.parse(jsonString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final long jsonLong = json.getAsLong();
                try {
                    return new Date(jsonLong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    };

    public static Gson make() {
        return new GsonBuilder()
                .registerTypeAdapter(RequestResult.class, new ResultJsonDeser())
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(Integer.class, intAsStringAdapter)
                .registerTypeAdapter(int.class, intAsStringAdapter)
                .registerTypeAdapter(Long.class, longAsStringAdapter)
                .registerTypeAdapter(long.class, longAsStringAdapter)
                .registerTypeAdapter(Date.class, dateAsStringAdapter)
                .serializeNulls()
                .create();

    }
}
