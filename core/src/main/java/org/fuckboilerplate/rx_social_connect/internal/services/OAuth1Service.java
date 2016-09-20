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
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth1AccessToken;
import org.fuckboilerplate.rx_social_connect.query_string.QueryString;
import org.fuckboilerplate.rx_social_connect.query_string.QueryStringStrategy;

public final class OAuth1Service extends Service<OAuth1AccessToken, OAuth10aService> {

    public OAuth1Service(OAuth10aService service) {
        super(service);
    }

    @Override public OAuth1AccessToken token(String url) throws Exception {
        Uri uri = Uri.parse(url);

        QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth1();

        String error = strategy.extractError(uri);
        if (error != null && !error.isEmpty()) throw new RuntimeException(error);

        String verifier = strategy.extractCode(uri);
        return new OAuth1AccessToken(
            service.getAccessToken(oAuth1RequestToken, verifier)
        );
    }

    private OAuth1RequestToken oAuth1RequestToken;
    @Override protected String authUrl() throws Exception {
        oAuth1RequestToken = service.getRequestToken();
        return service.getAuthorizationUrl(oAuth1RequestToken);
    }

    @Override public String callbackUrl() {
        return service.getConfig().getCallback();
    }
}
