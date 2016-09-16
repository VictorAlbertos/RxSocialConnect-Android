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

package org.fuckboilerplate.rx_social_connect.internal.services;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class Service<T extends Token, S extends OAuthService> {
    protected final S service;

    public Service(S service) {
        this.service = service;
    }

    public Observable<T> oResponse(final String url) {
        return Observable.defer(new Callable<ObservableSource<? extends T>>() {
            @Override public ObservableSource<? extends T> call() throws Exception {
                try {
                    return Observable.just(token(url));
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public abstract T token(String url) throws Exception;

    public Observable<String> oAuthUrl() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override public ObservableSource<? extends String> call() throws Exception {
                try {
                    return Observable.just(authUrl());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    protected abstract String authUrl() throws Exception;
    public abstract String callbackUrl();
}
