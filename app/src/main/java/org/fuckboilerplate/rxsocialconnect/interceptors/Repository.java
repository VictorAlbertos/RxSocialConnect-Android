package org.fuckboilerplate.rxsocialconnect.interceptors;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.YahooApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.victoralbertos.rx_social_connect.OAuth1Interceptor;
import io.victoralbertos.rx_social_connect.OAuth2Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Repository {
    private final YahooApiRest yahooApiRest;
    private final FacebookApiRest facebookApi;

    public Repository() {
        yahooApiRest = initYahooApiRest();
        facebookApi = initFacebookApiRest();
    }

    private FacebookApiRest initFacebookApiRest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OAuth2Interceptor(FacebookApi.class))
                .build();

        return new Retrofit.Builder()
                .baseUrl(FacebookApiRest.URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(FacebookApiRest.class);
    }

    private YahooApiRest initYahooApiRest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OAuth1Interceptor(yahooService()))
                .build();

        return new Retrofit.Builder()
                .baseUrl(YahooApiRest.URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(YahooApiRest.class);
    }

    FacebookApiRest getFacebookApi() {
        return facebookApi;
    }

    YahooApiRest getYahooApiRest() {
        return yahooApiRest;
    }

    interface YahooApiRest {
        String URL_BASE = "https://social.yahooapis.com";

        @GET("/v1/user/me/profile?format=json") Observable<Object> me();
    }

    interface FacebookApiRest {
        String URL_BASE = "https://graph.facebook.com";

        @GET("/v2.5/me")
        Observable<Object> me();
    }

    OAuth20Service facebookService() {
        String appId = "452930454916873";
        String appSecret = "4a643dd4c4537f01411ab7bb44736f1f";
        String callbackUrl = "http://victoralbertos.com/";
        String permissions = "public_profile, email";

        return new ServiceBuilder()
                .apiKey(appId)
                .apiSecret(appSecret)
                .callback(callbackUrl)
                .scope(permissions)
                .build(FacebookApi.instance());
    }

    OAuth10aService yahooService() {
        String clientId = "dj0yJmk9Sk9NZUlPc0RaODVDJmQ9WVdrOVRubHlWMWRuTTJVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wNw--";
        String clientSecret = "449b2d2f06d986297d65df7fc020544bf71eb10c";
        String callbackUrl = "http://victoralbertos.com";

        return new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .build(YahooApi.instance());
    }
}
