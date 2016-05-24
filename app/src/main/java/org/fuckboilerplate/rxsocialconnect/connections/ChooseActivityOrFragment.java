package org.fuckboilerplate.rxsocialconnect.connections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.fuckboilerplate.rxsocialconnect.R;

/**
 * Created by victor on 20/05/16.
 */
public class ChooseActivityOrFragment extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity_or_fragment);

        findViewById(R.id.bt_activity)
                .setOnClickListener(v -> startActivity(new Intent(this, ConnectionsActivity.class)));

        findViewById(R.id.bt_fragment)
                .setOnClickListener(v -> startActivity(new Intent(this, HostActivityFragment.class)));
    }
}
