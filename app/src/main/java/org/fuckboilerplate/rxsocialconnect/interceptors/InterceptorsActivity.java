package org.fuckboilerplate.rxsocialconnect.interceptors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.fuckboilerplate.rx_social_connect.RxSocialConnect;
import org.fuckboilerplate.rxsocialconnect.R;

public class InterceptorsActivity extends AppCompatActivity {
    private Repository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interceptors);
        repository = new Repository();

        setUpFacebook();
        setUpYahoo();
    }

    private void setUpFacebook() {
        findViewById(R.id.bt_connect_facebook).setOnClickListener(v ->
            RxSocialConnect.with(this, repository.facebookService())
                    .subscribe(response -> response.targetUI().showToken(response.token()),
                            error -> showError(error))
        );

        findViewById(R.id.bt_show_profile_facebook).setOnClickListener(v ->
                repository.getFacebookApi().me()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> showUserProfile(user.toString()),
                                    error -> showError(error))
        );
    }

    private void setUpYahoo() {
        findViewById(R.id.bt_connect_yahoo).setOnClickListener(v ->
                RxSocialConnect.with(this, repository.yahooService())
                        .subscribe(response -> response.targetUI().showToken(response.token()),
                                error -> showError(error))
        );

        findViewById(R.id.bt_show_profile_yahoo).setOnClickListener(v ->
                repository.getYahooApiRest().me()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> showUserProfile(user.toString()),
                                error -> showError(error))
        );
    }

    private void showUserProfile(String userProfileJSON) {
        clearTextViewToken();
        TextView tv_profile = (TextView) findViewById(R.id.tv_profile);
        tv_profile.setText(userProfileJSON);
    }

    private void showToken(OAuth2AccessToken oAuth2AccessToken) {
        clearTextViewToken();
        Toast.makeText(this, oAuth2AccessToken.getAccessToken(), Toast.LENGTH_SHORT).show();
    }

    private void showToken(OAuth1AccessToken token) {
        clearTextViewToken();
        Toast.makeText(this, token.getToken(), Toast.LENGTH_SHORT).show();
    }

    private void showError(Throwable error) {
        clearTextViewToken();
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void clearTextViewToken() {
        ((TextView) findViewById(R.id.tv_profile)).setText("");
    }
}
