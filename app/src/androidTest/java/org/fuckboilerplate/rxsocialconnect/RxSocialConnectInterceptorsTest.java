package org.fuckboilerplate.rxsocialconnect;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.fuckboilerplate.rxsocialconnect.interceptors.InterceptorsActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;

/**
 * To run the test uninstall the app previously and revoke permissions app from Facebook user profile.
 * Also, be aware that running these test will change the token value, so the unit tests of rx_social_connect_interceptors
 * related with yahoo and facebook will fail because the token will not longer be valid. So you need to update the token value
 * in the appropriate files after running this test.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RxSocialConnectInterceptorsTest {
    @Rule public ActivityTestRule<InterceptorsActivity> mActivityRule = new ActivityTestRule(InterceptorsActivity.class);

    @Test public void _a1_Connect_With_Facebook() {
        onView(withId(R.id.bt_connect_facebook)).perform(click());

        waitTime();
        onWebView()
                .withElement(findElement(Locator.NAME, "email"))
                .perform(DriverAtoms.webKeys(Credentials.EMAIL))
                .withElement(findElement(Locator.NAME, "pass"))
                .perform(DriverAtoms.webKeys(Credentials.PASSWORD))
                .withElement(findElement(Locator.NAME, "login"))
                .perform(webClick());

        waitTime();
        onWebView()
                .withElement(findElement(Locator.NAME, "__CONFIRM__"))
                .perform(webClick());
        waitTime();
    }

    @Test public void _a2_Show_Profile_Facebook() {
        onView(withId(R.id.tv_profile)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_show_profile_facebook)).perform(click());
        waitTime();
        onView(withId(R.id.tv_profile)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _b1_Connect_With_Yahoo() {
        onView(withId(R.id.bt_connect_yahoo)).perform(click());

        waitTime();
        onWebView()
                .withElement(findElement(Locator.NAME, "username"))
                .perform(DriverAtoms.webKeys(Credentials.EMAIL_YAHOO))
                .withElement(findElement(Locator.NAME, "signin"))
                .perform(webClick());

        waitTime();
        onWebView()
                .withElement(findElement(Locator.NAME, "passwd"))
                .perform(DriverAtoms.webKeys(Credentials.PASSWORD))
                .withElement(findElement(Locator.NAME, "signin"))
                .perform(webClick());

        waitTime();
        onWebView()
                .withElement(findElement(Locator.NAME, "agree"))
                .perform(webClick());
        waitTime();
    }

    @Test public void _b2_Show_Profile_Yahoo() {
        onView(withId(R.id.tv_profile)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_show_profile_yahoo)).perform(click());
        waitTime();
        onView(withId(R.id.tv_profile)).check(matches(shouldBeEmpty(false)));
    }

    private Matcher<View> shouldBeEmpty(boolean shouldBeEmpty) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override public void describeTo(Description description) {
                description.appendText("no empty text: ");
            }

            @Override public boolean matchesSafely(TextView textView) {
                String text = textView.getText().toString();
                if (shouldBeEmpty) return text.isEmpty();
                else return !text.isEmpty();
            }
        };
    }

    private void waitTime() {
        try {Thread.sleep(3500);}
        catch (InterruptedException e) { e.printStackTrace();}
    }
}