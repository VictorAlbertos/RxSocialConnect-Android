/*
 * Copyright 2016 FuckBoilerplate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fuckboilerplate.rx_social_connect.internal.persistence;

import com.github.scribejava.core.model.Token;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import rx.Observable;

public class Disk<T extends Token> {
    private final File cacheDirectory;
    private static final String NAME_DIR = "RxSocialConnect";

    public Disk(File file) {
        this.cacheDirectory = new File(file + File.separator + NAME_DIR);
        if (!this.cacheDirectory.exists()) cacheDirectory.mkdir();
    }

    public void save(String key, T data) {
        String wrapperJSONSerialized = new Gson().toJson(data);
        try {
            File file = new File(cacheDirectory, key);

            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(wrapperJSONSerialized);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Observable<T> get(String keyToken, Class<T> classToken) {
        T response = retrieve(keyToken, classToken);

        if (response != null) {
            if (response instanceof OAuth2AccessToken) {
                if (!((OAuth2AccessToken)response).isExpired()) {
                    return Observable.just(response);
                }
            } else {
                return Observable.just(response);
            }
        }

        return null;
    }

    private T retrieve(String key, Class<T> clazz) {
        try {
            File file = new File(cacheDirectory, key);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

            T data = new Gson().fromJson(bufferedReader, clazz);

            bufferedReader.close();
            return data;
        } catch (Exception ignore) {
            return null;
        }
    }

    public void evict(String key) {
        File file = new File(cacheDirectory, key);
        file.delete();
    }

    public void evictAll() {
        File[] tokens = cacheDirectory.listFiles();

        if (tokens == null) return;

        for (File token : tokens) {
            token.delete();
        }
    }
}
