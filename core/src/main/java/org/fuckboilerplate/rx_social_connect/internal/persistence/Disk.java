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
import io.reactivex.Observable;
import io.victoralbertos.jolyglot.Jolyglot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.fuckboilerplate.rx_social_connect.internal.encryption.BuiltInEncryptor;
import org.fuckboilerplate.rx_social_connect.internal.encryption.FileEncryptor;

public class Disk<T extends Token> {
    private final File cacheDirectory;
    private static final String NAME_DIR = "RxSocialConnect";
    private final Jolyglot jolyglot;
    private final String encryptionKey;
    private final FileEncryptor fileEncryptor;

    public Disk(File file, String encryptionKey, Jolyglot jolyglot) {
        this.fileEncryptor = new FileEncryptor(new BuiltInEncryptor());
        this.encryptionKey = encryptionKey;
        this.cacheDirectory = new File(file + File.separator + NAME_DIR);
        if (!this.cacheDirectory.exists()) cacheDirectory.mkdir();
        this.jolyglot = jolyglot;
    }

    public void save(String key, T data) {
        String wrapperJSONSerialized = jolyglot.toJson(data);
        FileWriter fileWriter = null;

        try {
            File file = new File(cacheDirectory, key);

            fileWriter = new FileWriter(file, false);
            fileWriter.write(wrapperJSONSerialized);
            fileWriter.flush();
            fileWriter.close();
            fileWriter = null;

            fileEncryptor.encrypt(encryptionKey, new File(cacheDirectory, key));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        File file = new File(cacheDirectory, key);
        file = fileEncryptor.decrypt(encryptionKey, file);

        try {
            T data = jolyglot.fromJson(file, clazz);
            file.delete();

            return data;
        } catch (Exception ignore) {
            return null;
        } finally {
            file.delete();
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
