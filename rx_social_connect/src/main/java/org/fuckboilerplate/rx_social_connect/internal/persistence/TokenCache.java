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

import android.content.Context;

import com.github.scribejava.core.model.Token;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;

public enum TokenCache {
    INSTANCE();

    private Disk disk;
    private ConcurrentMap<String, Observable<? extends Token>> memory;

    public void init(Context context) {
        disk = new Disk(context);
        memory = new ConcurrentHashMap();
    }

    public <T extends Token> void save(String key, T data) {
        memory.put(key, Observable.just(data));
        disk.save(key, data);
    }

    public Observable<? extends Token> get(String keyToken, Class<? extends Token> classToken) {
        Observable<? extends Token> token = memory.get(keyToken);
        if (token != null) return token;
        return disk.get(keyToken, classToken);
    }

    public void evict(String key) {
        memory.remove(key);
        disk.evict(key);
    }

    public void evictAll() {
        for (String key : memory.keySet()) {
            memory.remove(key);
        }
        disk.evictAll();
    }
}
