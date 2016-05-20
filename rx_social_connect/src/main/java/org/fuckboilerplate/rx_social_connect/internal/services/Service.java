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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public abstract class Service<T extends Token, S extends OAuthService> {
    protected final S service;

    public Service(S service) {
        this.service = service;
    }

    public Observable<T> oResponse(final String url) {
        return Observable.defer(new Func0<Observable<T>>() {
            @Override public Observable<T> call() {
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
        return Observable.defer(new Func0<Observable<String>>() {
            @Override public Observable<String> call() {
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
