package org.fuckboilerplate.rx_social_connect.internal.persistence;

public final class OAuth1AccessToken extends com.github.scribejava.core.model.OAuth1AccessToken {

  /**
   * Exists just to make happy some json providers
   */
  public OAuth1AccessToken() {
    super("stub", "stub", "stub");
  }

  public OAuth1AccessToken(com.github.scribejava.core.model.OAuth1AccessToken oAuth1AccessToken) {
    super(oAuth1AccessToken.getToken(),
        oAuth1AccessToken.getTokenSecret(),
        oAuth1AccessToken.getRawResponse());
  }

  public com.github.scribejava.core.model.OAuth1AccessToken toOAuth1AccessTokenScribe() {
    return this;
  }
}
