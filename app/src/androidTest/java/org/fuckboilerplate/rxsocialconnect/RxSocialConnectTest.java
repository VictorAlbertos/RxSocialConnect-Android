/*
 * Copyright 2016 FuckBoilerplate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fuckboilerplate.rxsocialconnect;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;

/**
 * Remove permissions for the app from facebook user profile before to run the test.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class RxSocialConnectTest {

    @Test public void _a1_Connect_With_Facebook() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));

        onView(withId(R.id.bt_facebook)).perform(click());

        waitTime();
        rotateDevice();

        onWebView()
                .withElement(findElement(Locator.NAME, "email"))
                .perform(DriverAtoms.webKeys(Credentials.EMAIL))
                .withElement(findElement(Locator.NAME, "pass"))
                .perform(DriverAtoms.webKeys(Credentials.PASSWORD))
                .withElement(findElement(Locator.NAME, "login"))
                .perform(webClick())
                .withElement(findElement(Locator.NAME, "__CONFIRM__"))
                .perform(webClick());

        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _a2_Connected_Facebook() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_facebook)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _a3_Get_Token_Facebook() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_facebook_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _a4_Disconnect_Facebook() {
        onView(withId(R.id.bt_facebook_disconnect)).perform(click());
        waitTime();
    }

    @Test public void _a5_Get_Token_Error_Facebook() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_facebook_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
    }

    @Test public void _a6_Facebook_Does_Not_Store_Cookies() {
        onView(withId(R.id.bt_facebook)).perform(click());
        waitTime();
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
    }

    @Test public void _b1_Connect_With_Google() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));

        onView(withId(R.id.bt_google)).perform(click());

        waitTime();
        rotateDevice();

        onWebView()
                .withElement(findElement(Locator.NAME, "Email"))
                .perform(DriverAtoms.webKeys(Credentials.EMAIL))
                .withElement(findElement(Locator.NAME, "signIn"))
                .perform(webClick());
        waitTime();

        onWebView()
                .withElement(findElement(Locator.NAME, "Passwd"))
                .perform(DriverAtoms.webKeys(Credentials.PASSWORD))
                .withElement(findElement(Locator.ID, "signIn"))
                .perform(webClick());

        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _b2_Connected_Google() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_google)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _b3_Get_Token_Google() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_google_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _b4_Disconnect_Google() {
        onView(withId(R.id.bt_google_disconnect)).perform(click());
        waitTime();
    }

    @Test public void _b5_Get_Token_Error_Google() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_google_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
    }

    @Test public void _b6_Google_Does_Not_Store_Cookies() {
        onView(withId(R.id.bt_google)).perform(click());
        waitTime();
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
    }

    @Test public void _d1_Connect_With_LinkedIn() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));

        onView(withId(R.id.bt_linkedin)).perform(click());

        waitTime();
        rotateDevice();

        onWebView()
                .withElement(findElement(Locator.NAME, "session_key"))
                .perform(DriverAtoms.webKeys(Credentials.EMAIL))
                .withElement(findElement(Locator.NAME, "session_password"))
                .perform(DriverAtoms.webKeys(Credentials.PASSWORD))
                .withElement(findElement(Locator.NAME, "authorize"))
                .perform(webClick());

        waitTime();
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _d2_Connected_LinkedIn() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_linkedin)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _d3_Get_Token_LinkedIn() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_linkedin_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _d4_Disconnect_LinkedIn() {
        onView(withId(R.id.bt_linkedin_disconnect)).perform(click());
        waitTime();
    }

    @Test public void _d5_Get_Token_Error_LinkedIn() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_linkedin_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
    }

    @Test public void _d6_LinkedIn_Does_Not_Store_Cookies() {
        onView(withId(R.id.bt_linkedin)).perform(click());
        waitTime();
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
    }

    @Test public void _e1_Connect_With_Yahoo() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));

        onView(withId(R.id.bt_yahoo)).perform(click());

        waitTime();
        rotateDevice();

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
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _e2_Connected_Yahoo() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_yahoo)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _e3_Get_Token_Yahoo() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_yahoo_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(false)));
    }

    @Test public void _e4_Disconnect_Yahoo() {
        onView(withId(R.id.bt_yahoo_disconnect)).perform(click());
        waitTime();
    }

    @Test public void _e5_Get_Token_Error_Yahoo() {
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
        onView(withId(R.id.bt_yahoo_get_token)).perform(click());
        waitTime();
        onView(withId(R.id.tv_token)).check(matches(shouldBeEmpty(true)));
    }

    @Test public void _e6_Yahoo_Does_Not_Store_Cookies() {
        onView(withId(R.id.bt_yahoo)).perform(click());
        waitTime();
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
    }

    //Twitter, github Can not be performed due to security restrictions.
    //Exception: Caused by: java.lang.RuntimeException: Fatal exception checking document state: Evaluation: status: 13 value: {message=Refused to evaluate a string as JavaScript because 'unsafe-eval' is not an allowed source of script in the following Content Security Policy directive: "script-src https://abs.twimg.com https://abs-0.twimg.com https://twitter.com https://mobile.twitter.com".
    //} hasMessage: true message: Refused to evaluate a string as JavaScript because 'unsafe-eval' is not an allowed source of script in the following Content Security Policy directive: "script-src https://abs.twimg.com https://abs-0.twimg.com https://twitter.com https://mobile.twitter.com".

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

    private void rotateDevice() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            uiDevice.setOrientationLeft();
            waitTime();
            uiDevice.setOrientationNatural();
            waitTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}