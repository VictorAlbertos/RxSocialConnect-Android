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

package org.fuckboilerplate.rx_social_connect.internal.services;

import android.net.Uri;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth2AccessToken;
import org.fuckboilerplate.rx_social_connect.query_string.QueryString;
import org.fuckboilerplate.rx_social_connect.query_string.QueryStringStrategy;

public final class OAuth2Service extends Service<OAuth2AccessToken, OAuth20Service> {

    public OAuth2Service(OAuth20Service service) {
        super(service);
    }

    @Override public OAuth2AccessToken token(String url) throws Exception {
        Uri uri = Uri.parse(url);

        QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth2();

        String error = strategy.extractError(uri);
        if (error != null && !error.isEmpty()) throw new RuntimeException(error);

        String code = strategy.extractCode(uri);
        com.github.scribejava.core.model.OAuth2AccessToken accessToken = service.getAccessToken(code);
        return new OAuth2AccessToken(accessToken);
    }

    @Override protected String authUrl() throws Exception {
        return service.getAuthorizationUrl();
    }

    @Override public String callbackUrl() {
        return service.getConfig().getCallback();
    }
}
