package org.fuckboilerplate.rxsocialconnect;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

/**
 * Created by victor on 20/05/16.
 */
public class ActivityTest extends RxSocialConnectTest {
    @Rule public ActivityTestRule<RxSocialConnectActivity> mActivityRule = new ActivityTestRule(RxSocialConnectActivity.class);
}
