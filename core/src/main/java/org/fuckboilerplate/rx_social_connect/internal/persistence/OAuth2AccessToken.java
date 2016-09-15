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

public final class OAuth2AccessToken extends com.github.scribejava.core.model.OAuth2AccessToken {
    private final long expirationDate;

    /**
     * Exists just to make happy some json providers
     */
    public OAuth2AccessToken() {
        super("stub", "stub", 0, "stub", "stub", "stub");
        expirationDate = 0;
    }

    public OAuth2AccessToken(com.github.scribejava.core.model.OAuth2AccessToken oAuth2AccessToken) {
        super(oAuth2AccessToken.getAccessToken(), oAuth2AccessToken.getTokenType(), oAuth2AccessToken.getExpiresIn(),
                oAuth2AccessToken.getRefreshToken(), oAuth2AccessToken.getScope(), oAuth2AccessToken.getRawResponse());

        if (oAuth2AccessToken.getExpiresIn() == null) this.expirationDate = 0;
        else this.expirationDate = System.currentTimeMillis() + (oAuth2AccessToken.getExpiresIn() * 1000);
    }

    public boolean isExpired() {
        if (expirationDate == 0) return false;
        return System.currentTimeMillis() > expirationDate;
    }

    public com.github.scribejava.core.model.OAuth2AccessToken toOAuth2AccessTokenScribe() {
        return this;
    }
}
