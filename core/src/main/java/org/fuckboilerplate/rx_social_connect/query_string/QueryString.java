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

package org.fuckboilerplate.rx_social_connect.query_string;

import android.net.Uri;

/**
 * Handle the current QueryStringStrategy and provide and entry point for further customization.
 *
 * @see QueryStringStrategy
 */
public enum QueryString {
  PARSER;

  private QueryStringStrategy strategyOAuth1;
  private QueryStringStrategy strategyOAuth2;

  QueryString() {
    strategyOAuth1 = defaultStrategyOAuth1();
    strategyOAuth2 = defaultStrategyOAuth2();
  }

  /**
   * Replace current QueryStringStrategy for OAuth1 providers
   */
  public void replaceStrategyOAuth1(QueryStringStrategy strategyOauth1) {
    this.strategyOAuth1 = strategyOauth1;
  }

  /**
   * Replace current QueryStringStrategy for OAuth2 providers
   */
  public void replaceStrategyOAuth2(QueryStringStrategy strategyOauth2) {
    this.strategyOAuth2 = strategyOauth2;
  }

  /**
   * Reset QueryStringStrategy for OAuth1 providers to default
   */
  public void resetStrategyOAuth1() {
    this.strategyOAuth1 = defaultStrategyOAuth1();
  }

  /**
   * Reset QueryStringStrategy for OAuth2 providers to default
   */
  public void resetStrategyOAuth2() {
    this.strategyOAuth2 = defaultStrategyOAuth2();
  }

  public QueryStringStrategy getStrategyOAuth1() {
    return strategyOAuth1;
  }

  public QueryStringStrategy getStrategyOAuth2() {
    return strategyOAuth2;
  }

  private QueryStringStrategy defaultStrategyOAuth1() {
    return new QueryStringStrategy() {
      @Override public String extractCode(Uri uri) {
        return uri.getQueryParameter("oauth_verifier");
      }

      @Override public String extractError(Uri uri) {
        return uri.getQueryParameter("error");
      }
    };
  }

  private QueryStringStrategy defaultStrategyOAuth2() {
    return new QueryStringStrategy() {
      @Override public String extractCode(Uri uri) {
        return uri.getQueryParameter("code");
      }

      @Override public String extractError(Uri uri) {
        return uri.getQueryParameter("error");
      }
    };
  }
}