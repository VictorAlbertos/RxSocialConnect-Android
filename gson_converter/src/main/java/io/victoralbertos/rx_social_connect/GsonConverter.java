package io.victoralbertos.rx_social_connect;

import com.google.gson.Gson;

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
