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

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.victoralbertos.rx_social_connect.base.OAuth1InterceptorTest;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public class OAuth1TwitterInterceptorTest extends OAuth1InterceptorTest {

    @Override protected OAuth10aService getService() {
        String consumerKey = "3wEAwhjvJmBpJpUfIXiY9PQOg";
        String consumerSecret = "8N47XdpE4GSlswMcBTOZFc3fDcil7WnK4RINfUyKf2d0rFrF2I";

        return new ServiceBuilder()
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .build(TwitterApi.instance());
    }

    @Override protected OAuth1AccessToken getToken() {
        String token = "732545483974053888-YD0xBTx9261ghVNRxTnxTqDd6feBF7h";
        String tokenSecret = "kwXNB0IzLtH5aZA9T5Fvg5Gh1cLVjayAFWfDYWV3AtW1p";

        OAuth1AccessToken twitterToken = new OAuth1AccessToken(token, tokenSecret);
        return twitterToken;
    }

    @Override protected Observable<Object> getApiCallWithoutInterceptor() {
        return new Retrofit.Builder()
                .baseUrl(OAuth1Api.URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(OAuth1Api.class)
                .me();
    }

    @Override protected Observable<Object> getApiCallWithInterceptor(OAuth1Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(OAuth1Api.URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(OAuth1Api.class)
                .me();
    }

    private interface OAuth1Api {
        String URL_BASE = "https://api.twitter.com";

        @GET("/1.1/account/verify_credentials.json")
        Observable<Object> me();
    }
}
