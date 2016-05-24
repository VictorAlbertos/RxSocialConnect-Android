package org.fuckboilerplate.rxsocialconnect;

import android.app.Application;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import io.victoralbertos.rx_social_connect_gson_converter.GsonConverter;

/**
 * Created by victor on 17/05/16.
 */
public class SampleApp extends Application {

    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect
                .register(this)
                .using(GsonConverter.create());
    }
}
