package org.fuckboilerplate.rxsocialconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.apis.TwitterApi;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

public class RxSocialConnectActivity extends AppCompatActivity {
    private Helper helper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        helper = new Helper(this);

        setUpTwitter();
        setUpFacebook();
        setUpGoogle();
        setUpLinkedIn();

        findViewById(R.id.bt_all_disconnect).setOnClickListener(v -> helper.closeAllConnection());
    }

    private void setUpTwitter() {
        findViewById(R.id.bt_twitter).setOnClickListener(v -> {
            RxSocialConnect.with(RxSocialConnectActivity.this, helper.twitterService())
                    .subscribe(response -> response.targetUI().helper.showResponse(response.token()),
                            error -> helper.showError(error));
        });

        findViewById(R.id.bt_twitter_get_token).setOnClickListener(v -> helper.showTokenOAuth1(TwitterApi.class));

        findViewById(R.id.bt_twitter_disconnect).setOnClickListener(v -> helper.closeConnection(TwitterApi.class));
    }

    private void setUpFacebook() {
        findViewById(R.id.bt_facebook).setOnClickListener(v -> {
            RxSocialConnect.with(RxSocialConnectActivity.this, helper.facebookService())
                    .subscribe(response -> response.targetUI().helper.showResponse(response.token()),
                            error -> helper.showError(error));
        });

        findViewById(R.id.bt_facebook_get_token).setOnClickListener(v -> helper.showTokenOAuth2(FacebookApi.class));

        findViewById(R.id.bt_facebook_disconnect).setOnClickListener(v -> helper.closeConnection(FacebookApi.class));
    }

    private void setUpGoogle() {
        findViewById(R.id.bt_google).setOnClickListener(v -> {
            RxSocialConnect.with(RxSocialConnectActivity.this, helper.googleService())
                    .subscribe(response -> response.targetUI().helper.showResponse(response.token()),
                            error -> helper.showError(error));
        });

        findViewById(R.id.bt_google_get_token).setOnClickListener(v -> helper.showTokenOAuth2(GoogleApi20.class));

        findViewById(R.id.bt_google_disconnect).setOnClickListener(v -> helper.closeConnection(GoogleApi20.class));
    }

    private void setUpLinkedIn() {
        findViewById(R.id.bt_linkedin).setOnClickListener(v -> {
            RxSocialConnect.with(RxSocialConnectActivity.this, helper.linkedinService())
                    .subscribe(response -> response.targetUI().helper.showResponse(response.token()),
                            error -> helper.showError(error));
        });

        findViewById(R.id.bt_linkedin_get_token).setOnClickListener(v -> helper.showTokenOAuth2(LinkedInApi20.class));

        findViewById(R.id.bt_linkedin_disconnect).setOnClickListener(v -> helper.closeConnection(LinkedInApi20.class));
    }
}
