package org.fuckboilerplate.rxsocialconnect.connections;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.apis.YahooApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;
import org.fuckboilerplate.rxsocialconnect.R;

public class Helper {
    private final Object targetUI;

    public Helper(Object targetUI) {
        this.targetUI = targetUI;
    }

    OAuth10aService twitterService() {
        String consumerKey = "3wEAwhjvJmBpJpUfIXiY9PQOg";
        String consumerSecret = "8N47XdpE4GSlswMcBTOZFc3fDcil7WnK4RINfUyKf2d0rFrF2I";
        String callbackUrl = "http://victoralbertos.com";

        return new ServiceBuilder()
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .callback(callbackUrl)
                .build(TwitterApi.instance());
    }

    OAuth20Service facebookService() {
        String appId = "452930454916873";
        String appSecret = "4a643dd4c4537f01411ab7bb44736f1f";
        String callbackUrl = "http://victoralbertos.com/";
        String permissions = "public_profile, email";

        return new ServiceBuilder()
                .apiKey(appId)
                .apiSecret(appSecret)
                .callback(callbackUrl)
                .scope(permissions)
                .build(FacebookApi.instance());
    }

    OAuth20Service googleService() {
        String clientId = "112202070176-3b8b2s85rtt39k6ga5f2001p937i57fq.apps.googleusercontent.com";
        String clientSecret = "-zkjJwn3j_2JOyPSDHExJ6cO";
        String callbackUrl = "http://victoralbertos.com/";
        String permissions = "profile";

        return new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .scope(permissions)
                .build(GoogleApi20.instance());
    }

    OAuth20Service linkedinService() {
        String clientId = "77u9plrpoq0g6t";
        String clientSecret = "VlH229TNkzJysxbq";
        String callbackUrl = "http://victoralbertos.com";
        String permissions = "r_basicprofile";

        return new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .scope(permissions)
                .build(LinkedInApi20.instance());
    }

    OAuth10aService yahooService() {
        String clientId = "dj0yJmk9Sk9NZUlPc0RaODVDJmQ9WVdrOVRubHlWMWRuTTJVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wNw--";
        String clientSecret = "449b2d2f06d986297d65df7fc020544bf71eb10c";
        String callbackUrl = "http://victoralbertos.com";

        return new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .build(YahooApi.instance());
    }

    OAuth20Service githubService() {
        String appId = "725a59980165855cc986";
        String appSecret = "38733b4bf1c8646af86e38797f345847a0b61388";
        String callbackUrl = "http://victoralbertos.com/";

        return new ServiceBuilder()
            .apiKey(appId)
            .apiSecret(appSecret)
            .callback(callbackUrl)
            .build(GitHubApi.instance());
    }

    void showTokenOAuth1(Class<? extends DefaultApi10a> clazz) {
        RxSocialConnect.getTokenOAuth1(clazz)
                .subscribe(token -> showResponse(token),
                        error -> showError(error));
    }

    void showTokenOAuth2(Class<? extends DefaultApi20> clazz) {
        RxSocialConnect.getTokenOAuth2(clazz)
                .subscribe(token -> showResponse(token),
                        error -> showError(error));
    }

    void closeConnection(Class<? extends BaseApi> clazz) {
        RxSocialConnect
                .closeConnection(clazz)
                .subscribe(_I -> {
                    showToast(clazz.getName() + " disconnected");
                    clearEditTexts();
                }, error -> showError(error));
    }

    void closeAllConnection() {
        RxSocialConnect
                .closeConnections()
                .subscribe(_I -> {
                    showToast("All disconnected");
                    clearEditTexts();
                }, error -> showError(error));
    }

    void showResponse(OAuth1AccessToken token) {
        clearEditTexts();
        ((TextView)findViewById(R.id.tv_token)).setText(token.getToken());
        ((TextView)findViewById(R.id.tv_secret)).setText(token.getTokenSecret());
    }

    void showResponse(OAuth2AccessToken token) {
        clearEditTexts();
        ((TextView)findViewById(R.id.tv_token)).setText(token.getAccessToken());
    }

    private void clearEditTexts() {
        ((TextView)findViewById(R.id.tv_token)).setText("");
        ((TextView)findViewById(R.id.tv_secret)).setText("");
    }

    void showError(Throwable throwable) {
        clearEditTexts();
        showToast(throwable.getMessage());
    }

    private View findViewById(int resId) {
        if (targetUI instanceof Activity) return ((Activity) targetUI).findViewById(resId);
        else return ((Fragment) targetUI).getView().findViewById(resId);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private Context getContext() {
        if (targetUI instanceof Activity) return  ((Activity) targetUI);
        else return((Fragment) targetUI).getActivity();
    }
}
