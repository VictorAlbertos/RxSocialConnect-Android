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

import android.net.Uri;
import android.support.annotation.Nullable;
import org.fuckboilerplate.rx_social_connect.query_string.QueryString;
import org.fuckboilerplate.rx_social_connect.query_string.QueryStringStrategy;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class QueryStringTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock Uri uri;

  @Test public void _1_Verify_Replace_OAuth1_Strategy() {
    when(uri.getQueryParameter("oauth_verifier_custom")).thenReturn("success");
    when(uri.getQueryParameter("error_custom")).thenReturn("error");

    QueryString.PARSER.replaceStrategyOAuth1(new QueryStringStrategy() {
      @Override public String extractCode(Uri uri) {
        return uri.getQueryParameter("oauth_verifier_custom");
      }

      @Nullable @Override public String extractError(Uri uri) {
        return uri.getQueryParameter("error_custom");
      }
    });

    QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth1();
    assertThat(strategy.extractCode(uri), is("success"));
    assertThat(strategy.extractError(uri), is("error"));
  }

  @Test public void _2_Verify_Replace_OAuth2_Strategy() {
    when(uri.getQueryParameter("code_custom")).thenReturn("success");
    when(uri.getQueryParameter("error_custom")).thenReturn("error");

    QueryString.PARSER.replaceStrategyOAuth2(new QueryStringStrategy() {
      @Override public String extractCode(Uri uri) {
        return uri.getQueryParameter("code_custom");
      }

      @Nullable @Override public String extractError(Uri uri) {
        return uri.getQueryParameter("error_custom");
      }
    });

    QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth2();
    assertThat(strategy.extractCode(uri), is("success"));
    assertThat(strategy.extractError(uri), is("error"));
  }

  @Test public void _3_Verify_Default_OAuth1_Strategy() {
    QueryString.PARSER.resetStrategyOAuth1();

    when(uri.getQueryParameter("oauth_verifier")).thenReturn("success");
    when(uri.getQueryParameter("error")).thenReturn("error");

    QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth1();
    assertThat(strategy.extractCode(uri), is("success"));
    assertThat(strategy.extractError(uri), is("error"));
  }

  @Test public void _4_Verify_Default_OAuth2_Strategy() {
    QueryString.PARSER.resetStrategyOAuth2();

    when(uri.getQueryParameter("code")).thenReturn("success");
    when(uri.getQueryParameter("error")).thenReturn("error");

    QueryStringStrategy strategy = QueryString.PARSER.getStrategyOAuth2();
    assertThat(strategy.extractCode(uri), is("success"));
    assertThat(strategy.extractError(uri), is("error"));
  }
}