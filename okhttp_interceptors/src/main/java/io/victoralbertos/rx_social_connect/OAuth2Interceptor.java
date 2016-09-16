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

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import io.reactivex.Observable;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.fuckboilerplate.rx_social_connect.NotActiveTokenFoundException;
import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

public class OAuth2Interceptor implements Interceptor {
    private final Class<? extends DefaultApi20> apiClass;

    public OAuth2Interceptor(Class<? extends DefaultApi20> apiClass) {
        this.apiClass = apiClass;
    }

    @Override public Response intercept(Chain chain) throws IOException, NotActiveTokenFoundException {
        String token = getOToken().blockingFirst().getAccessToken();
        Request request = chain.request();

        Request.Builder builderRequest = request.newBuilder();
        builderRequest.addHeader("Authorization", "Bearer " + token);
        request = builderRequest.build();

        Response response = chain.proceed(request);

        return response;
    }

    //Exists for testing purposes
    protected Observable<OAuth2AccessToken> getOToken() {
        return RxSocialConnect.getTokenOAuth2(apiClass);
    }
}
