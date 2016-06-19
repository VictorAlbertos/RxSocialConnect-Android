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

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.TwitterApi;
import io.victoralbertos.jolyglot.GsonSpeaker;
import io.victoralbertos.jolyglot.Jolyglot;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenCacheTest {
    private static final String TOKEN_KEY = TwitterApi.class.getSimpleName(), TOKEN_KEY_2 = FacebookApi.class.getSimpleName(), TOKEN = "TOKEN", TOKEN_SECRET = "TOKEN_SECRET", RAW_RESPONSE = "RAW_RESPONSE";
    private static final com.github.scribejava.core.model.OAuth1AccessToken OAUTH1_ACCESS_TOKEN = new com.github.scribejava.core.model.OAuth1AccessToken(TOKEN, TOKEN_SECRET, RAW_RESPONSE);
    private static final com.github.scribejava.core.model.OAuth2AccessToken OAUTH2_ACCESS_TOKEN = new com.github.scribejava.core.model.OAuth2AccessToken(TOKEN, RAW_RESPONSE);

    @ClassRule public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test public void _1_When_Save_Token_And_Retrieve_It_Get_It() {
        TokenCache.INSTANCE.init(temporaryFolder.getRoot(), "key", jsonConverter());

        TokenCache.INSTANCE.save(TOKEN_KEY, OAUTH1_ACCESS_TOKEN);

        TestSubscriber<com.github.scribejava.core.model.OAuth1AccessToken> testSubscriber = new TestSubscriber<>();
        Observable<com.github.scribejava.core.model.OAuth1AccessToken> oToken = (Observable<com.github.scribejava.core.model.OAuth1AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY, OAuth1AccessToken.class);
        oToken.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        assertThat(testSubscriber.getOnNextEvents().get(0).getToken(), is(TOKEN));
    }

    @Test public void _2_When_Retrieve_After_Memory_Destroyed_Get_It() {
        TokenCache.INSTANCE.init(temporaryFolder.getRoot(), "key", jsonConverter());

        TestSubscriber<com.github.scribejava.core.model.OAuth1AccessToken> testSubscriber = new TestSubscriber<>();
        Observable<com.github.scribejava.core.model.OAuth1AccessToken> oToken = (Observable<com.github.scribejava.core.model.OAuth1AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY, OAuth1AccessToken.class);
        oToken.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        assertThat(testSubscriber.getOnNextEvents().get(0).getToken(), is(TOKEN));
    }

    @Test public void _3_When_Evict_And_Retrieve_It_Get_Null() {
        TokenCache.INSTANCE.init(temporaryFolder.getRoot(), "key", jsonConverter());

        TokenCache.INSTANCE.evict(TOKEN_KEY);
        Observable<com.github.scribejava.core.model.OAuth1AccessToken> oToken = (Observable<com.github.scribejava.core.model.OAuth1AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY, OAuth1AccessToken.class);
        assertNull(oToken);
    }

    @Test public void _4_When_Evict_All_And_Retrieve_It_Get_Null() {
        TokenCache.INSTANCE.init(temporaryFolder.getRoot(), "key", jsonConverter());

        TokenCache.INSTANCE.save(TOKEN_KEY, OAUTH1_ACCESS_TOKEN);
        TokenCache.INSTANCE.save(TOKEN_KEY_2, OAUTH2_ACCESS_TOKEN);

        TestSubscriber<com.github.scribejava.core.model.OAuth1AccessToken> testSubscriberToken1 = new TestSubscriber<>();
        Observable<com.github.scribejava.core.model.OAuth1AccessToken> oToken = (Observable<com.github.scribejava.core.model.OAuth1AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY, OAuth1AccessToken.class);
        oToken.subscribe(testSubscriberToken1);
        testSubscriberToken1.awaitTerminalEvent();

        assertThat(testSubscriberToken1.getOnNextEvents().get(0).getToken(), is(TOKEN));

        TestSubscriber<com.github.scribejava.core.model.OAuth2AccessToken> testSubscriberToken2 = new TestSubscriber<>();
        Observable<com.github.scribejava.core.model.OAuth2AccessToken> oToken2 = (Observable<com.github.scribejava.core.model.OAuth2AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY_2, OAuth2AccessToken.class);
        oToken2.subscribe(testSubscriberToken2);
        testSubscriberToken2.awaitTerminalEvent();

        assertThat(testSubscriberToken2.getOnNextEvents().get(0).getAccessToken(), is(TOKEN));

        TokenCache.INSTANCE.evict(TOKEN_KEY);
        TokenCache.INSTANCE.evict(TOKEN_KEY_2);

        oToken = (Observable<com.github.scribejava.core.model.OAuth1AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY, OAuth1AccessToken.class);
        assertNull(oToken);

        oToken2 = (Observable<com.github.scribejava.core.model.OAuth2AccessToken>) TokenCache.INSTANCE.get(TOKEN_KEY_2, OAuth2AccessToken.class);
        assertNull(oToken2);
    }

    private Jolyglot jsonConverter() {
        return new GsonSpeaker();
    }

}