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

package org.fuckboilerplate.rx_social_connect;


import org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth2AccessToken;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OAuth2AccessTokenTest {

    @Test public void When_Token_Expires_Then_Expire() throws Exception {
        Integer expiresInOneSecond = 3;
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(createToken(expiresInOneSecond));

        waitTime(3500);

        assertThat(oAuth2AccessToken.isExpired(), is(true));
    }

    @Test public void When_Token_Not_Expire_Then_Not_Expire() throws Exception {
        Integer expiresInOneSecond = 1;
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(createToken(expiresInOneSecond));

        waitTime(550);

        assertThat(oAuth2AccessToken.isExpired(), is(false));
    }

    private com.github.scribejava.core.model.OAuth2AccessToken createToken(Integer expiresOnSeconds) {
        return new com.github.scribejava.core.model.OAuth2AccessToken("", "", expiresOnSeconds, "", "", "");
    }

    private void waitTime(long time) {
        try {Thread.sleep(time);}
        catch (InterruptedException e) { e.printStackTrace();}
    }

}