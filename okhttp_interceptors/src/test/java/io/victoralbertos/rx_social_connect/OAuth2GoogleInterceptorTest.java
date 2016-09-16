/*
 * Copyright 2016 Copyright 2016 Víctor Albertos
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

package io.victoralbertos.rx_social_connect;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.victoralbertos.rx_social_connect.base.OAuth2InterceptorTest;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class OAuth2GoogleInterceptorTest extends OAuth2InterceptorTest {

    @Override protected Observable<Object> getApiCallWithoutInterceptor() {
        return new Retrofit.Builder()
                .baseUrl(OAuth2Api.URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(OAuth2Api.class)
                .me();
    }

    @Override protected Observable<Object> getApiCallWithInterceptor(OAuth2Interceptor oAuth2Interceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(oAuth2Interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(OAuth2Api.URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(OAuth2Api.class)
                .me();
    }

    @Override protected OAuth2AccessToken getToken() {
        String accessToken = "ya29.CjLsAkgBIih0ud7W_a82uKsm6nIWbjg4YuBnK0smznICod4npYiVwz08YB6jPznOHpC6Ig";
        OAuth2AccessToken token = new OAuth2AccessToken(accessToken);
        return token;
    }


    interface OAuth2Api {
        String URL_BASE = "https://www.googleapis.com";

        @GET("/plus/v1/people/me")
        Observable<Object> me();
    }
}
