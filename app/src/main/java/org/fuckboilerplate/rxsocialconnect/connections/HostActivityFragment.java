package org.fuckboilerplate.rxsocialconnect.connections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.fuckboilerplate.rxsocialconnect.R;

/**
 * Created by victor on 20/05/16.
 */
public class HostActivityFragment extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_fragment);
    }

}
