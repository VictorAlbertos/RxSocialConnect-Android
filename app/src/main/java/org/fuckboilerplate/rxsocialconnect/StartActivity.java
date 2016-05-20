package org.fuckboilerplate.rxsocialconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by victor on 20/05/16.
 */
public class StartActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        findViewById(R.id.bt_activity)
                .setOnClickListener(v -> startActivity(new Intent(this, RxSocialConnectActivity.class)));

        findViewById(R.id.bt_fragment)
                .setOnClickListener(v -> startActivity(new Intent(this, HostActivityFragment.class)));
    }
}
