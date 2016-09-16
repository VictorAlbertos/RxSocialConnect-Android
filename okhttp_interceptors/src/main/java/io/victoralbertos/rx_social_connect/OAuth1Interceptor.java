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

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.reactivex.Observable;
import org.fuckboilerplate.rx_social_connect.NotActiveTokenFoundException;
import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuth1Interceptor implements Interceptor {
    protected final OAuth10aService service;

    public OAuth1Interceptor(OAuth10aService service) {
        this.service = service;
    }

    @Override public Response intercept(Chain chain) throws IOException, NotActiveTokenFoundException {
        Request request = chain.request();

        String url = request.url().toString();
        Verb verb = apply(request.method());

        OAuthRequest requestOAuth = new OAuthRequest(verb, url, service);
        Request.Builder builderRequest = request.newBuilder();

        OAuth1AccessToken token = getOToken().blockingFirst();
        service.signRequest(token, requestOAuth);

        Map<String, String> headers = requestOAuth.getHeaders();

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builderRequest.addHeader(header.getKey(), header.getValue());
        }

        Response response = chain.proceed(builderRequest.build());
        return response;
    }

    private Verb apply(String method) {
        if (method.equalsIgnoreCase("GET")) return Verb.GET;
        else if (method.equalsIgnoreCase("POST")) return Verb.POST;
        else if (method.equalsIgnoreCase("PUT")) return Verb.PUT;
        else if (method.equalsIgnoreCase("DELETE")) return Verb.DELETE;
        else if (method.equalsIgnoreCase("HEAD")) return Verb.HEAD;
        else if (method.equalsIgnoreCase("OPTIONS")) return Verb.OPTIONS;
        else if (method.equalsIgnoreCase("TRACE")) return Verb.TRACE;
        else if (method.equalsIgnoreCase("PATCH")) return Verb.PATCH;
        return null;
    }

    //Exists for testing purposes
    protected Observable<OAuth1AccessToken> getOToken() {
        return RxSocialConnect.getTokenOAuth1(service.getApi().getClass());
    }
}
