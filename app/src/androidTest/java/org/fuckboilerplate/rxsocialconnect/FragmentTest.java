package org.fuckboilerplate.rxsocialconnect;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

/**
 * Created by victor on 20/05/16.
 */
public class FragmentTest extends RxSocialConnectTest {
    @Rule public ActivityTestRule<HostActivityFragment> mActivityRule = new ActivityTestRule(HostActivityFragment.class);
}
