package io.victoralbertos.rx_social_connect_gson_converter;

import com.google.gson.Gson;

import org.fuckboilerplate.rx_social_connect.JSONConverter;

import java.io.File;
import java.io.FileReader;

/**
 * JSONConverter implementation using gson.
 * @see JSONConverter
 */
public class GsonConverter implements JSONConverter {

    public static GsonConverter create() {
        return new GsonConverter();
    }

    private GsonConverter() {}

    @Override public <T> T fromJson(File file, Class<T> clazz) throws Exception {
        T data = new Gson().fromJson(new FileReader(file.getAbsoluteFile()), clazz);
        return data;
    }

    @Override public String toJson(Object object) {
        return new Gson().toJson(object);
    }
}
