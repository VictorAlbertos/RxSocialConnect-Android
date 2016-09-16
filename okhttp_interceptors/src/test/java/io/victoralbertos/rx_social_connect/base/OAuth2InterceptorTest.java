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

package io.victoralbertos.rx_social_connect.base;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.victoralbertos.rx_social_connect.OAuth2Interceptor;
import java.io.IOException;
import org.fuckboilerplate.rx_social_connect.NotActiveTokenFoundException;
import org.junit.Test;

public abstract class OAuth2InterceptorTest {

    @Test public void Get_Method_Throws_HttpException_Without_Interceptor() throws IOException {
        TestObserver<Object> testSubscriber = new TestObserver<>();

        getApiCallWithoutInterceptor().subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoValues();
        testSubscriber.assertError(HttpException.class);
    }

    @Test public void Get_Method_Throws_NotActiveTokenFoundException_With_Interceptor_With_Invalid_Token() throws IOException {
        OAuth2Interceptor interceptor = new OAuthInterceptorInvalidToken(null);

        TestObserver<Object> testSubscriber = new TestObserver<>();

        getApiCallWithInterceptor(interceptor).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoValues();
        testSubscriber.assertError(NotActiveTokenFoundException.class);
    }

    @Test public void Get_Method_Success_With_Interceptor_With_Valid_Token() throws IOException {
        OAuth2Interceptor interceptor = new OAuthInterceptorValidToken(null);
        TestObserver<Object> testSubscriber = new TestObserver<>();

        getApiCallWithInterceptor(interceptor).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertNoErrors();
    }

    private class OAuthInterceptorInvalidToken extends OAuth2Interceptor {
        public OAuthInterceptorInvalidToken(Class<? extends DefaultApi20> apiClass) {
            super(apiClass);
        }

        @Override protected Observable<OAuth2AccessToken> getOToken() {
            return Observable.error(new NotActiveTokenFoundException());
        }
    }

    private class OAuthInterceptorValidToken extends OAuth2Interceptor {

        public OAuthInterceptorValidToken(Class<? extends DefaultApi20> apiClass) {
            super(apiClass);
        }

        @Override protected Observable<OAuth2AccessToken> getOToken() {
            return Observable.just(getToken());
        }
    }

    protected abstract Observable<Object> getApiCallWithoutInterceptor();
    protected abstract Observable<Object> getApiCallWithInterceptor(OAuth2Interceptor oAuth2Interceptor);
    protected abstract OAuth2AccessToken getToken();
}
