package org.fuckboilerplate.rxsocialconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.fuckboilerplate.rxsocialconnect.connections.ChooseActivityOrFragment;
import org.fuckboilerplate.rxsocialconnect.interceptors.InterceptorsActivity;

/**
 * Created by victor on 24/05/16.
 */
public class StartActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        findViewById(R.id.bt_connections).setOnClickListener(v ->
                startActivity(new Intent(this, ChooseActivityOrFragment.class))
        );

        findViewById(R.id.bt_interceptors).setOnClickListener(v ->
                startActivity(new Intent(this, InterceptorsActivity.class))
        );
    }

}
