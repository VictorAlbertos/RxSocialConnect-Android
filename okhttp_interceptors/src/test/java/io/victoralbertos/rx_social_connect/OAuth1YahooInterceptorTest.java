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

import com.github.scribejava.apis.YahooApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.victoralbertos.rx_social_connect.base.OAuth1InterceptorTest;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class OAuth1YahooInterceptorTest extends OAuth1InterceptorTest {

    @Override protected OAuth10aService getService() {
        String clientId = "dj0yJmk9Sk9NZUlPc0RaODVDJmQ9WVdrOVRubHlWMWRuTTJVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wNw--";
        String clientSecret = "449b2d2f06d986297d65df7fc020544bf71eb10c";

        return new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .build(YahooApi.instance());
    }

    @Override protected OAuth1AccessToken getToken() {
        String token = "A=bKGw61fClQst5jvXVpOpwRyYXz..2es7g7C7m5k2_HwuKSKfi6VTnJeJJdGnaRRuISwGZcCRQ3J4ldwcbezZL0giYmYZDIZb_OcPttEmpwYneb030se5gWj4yPIKhciYtwruba8CKk6B96MEtPKIfNZ7sdeAB0MPRAGEe_.gZl9k.4L72qFoWzStHpTd79Wg3r8M3ClfFIT64zajfIldkLatEesiE6mqdfWLbNlJ1Zp8QJ.WSJq_aSc.rHZQ_gCx6J6QMMImF9BzCuevhoYN75G9aGkE_28HnqZUm5mCozGGx4ILXTDp4V_CWAL02M5wq1qC.28k8d7z0HnbXx80Y6PeIayqfog0BKaaY1GeM4elGY7kVBVNu5BzlsLfKOUXzRYS3JfYHWYxB3B3fdixE1DKFItaJ3bukVlTksZkgS6oYp3cmXF9iBEIxkmYr0B9u33jAn9Rc3cLfMF9u8LqsTUdVVkCO46dMvGvNwFCW410VelgVvG6lYfaFNtNMluBtyaDYjpXf2XH1Fx7nQ65Ma7CVrKr5EK61rXGYc1OwhsCn88IDUTShdqWW0xTk1sAsdz75pmofw5iR9ehZEJNeHKoDNLxihhBSLT5HC8eRl_FOgA7yuePnJceJuM._Jhsdm6BB2ocLKDEU3s2h.Djk9KLq640s6KfTR.AjM.G9.4DVrHiTA0JA2VxYPXWqOkrbfidIKthITd0ffIsMi6Tghw2vxYSWsF2oe6NrHdtTU04ahAuY3sRsEII04R9YDO.GeTVAb915yL9mo7ELQ_wjddxfh38rdDOeEfNotsfgMaAZB2zsDvMPtmIX9123jkdSyRf19aMy7viifHdi0zN.HHew4QGD3p04DYuJ60VweMrXoHfCvw605D2gLjvEvmN";
        String tokenSecret = "f021440f68b8d9141849df4c233d3524789eaca8";

        OAuth1AccessToken twitterToken = new OAuth1AccessToken(token, tokenSecret);
        return twitterToken;
    }

    @Override protected Observable<Object> getApiCallWithoutInterceptor() {
        return new Retrofit.Builder()
                .baseUrl(OAuth1Api.URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(OAuth1Api.class)
                .me();
    }

    private interface OAuth1Api {
        String URL_BASE = "https://social.yahooapis.com";

        @GET("/v1/user/me/profile?format=json")
        Observable<Object> me();
    }
}


