[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxSocialConnect--Android-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3616)

OAuth RxJava extension for Android. iOS version is located at this [repository](https://github.com/FuckBoilerplate/RxSocialConnect-iOS).

# RxSocialConnect

RxSocialConnect simplifies the process of retrieving authorizations tokens from multiple social networks to a minimalist observable call, from any Fragment or Activity.

```java
OAuth20Service facebookService = //...

RxSocialConnect.with(fragmentOrActivity, facebookService)
                    .subscribe(response -> response.targetUI().showResponse(response.token()));
```

## Features:

* Webview implementation to handle the sequent steps of oauth process.
* Storage of tokens encrypted locally
* Automatic refreshing tokens taking care of expiration date.
* I/O operations performed on secondary threads and automatic sync with user interface on the main thread, thanks to [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* Mayor social network supported, more than 16 providers; including Facebook, Twitter, GooglePlus, LinkedIn and so on. Indeed, it supports as many providers as [ScribeJava](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples) does, because RxSocialConnect is a reactive-android wrapper around it.
* Honors the observable chain. [RxOnActivityResult](https://github.com/VictorAlbertos/RxActivityResult) allows RxSocialConnect to transform every oauth process into an observable for a wonderful chaining process.

## Setup

Add the JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of android app module:
```gradle
dependencies {
    compile 'com.github.VictorAlbertos.RxSocialConnect-Android:core:1.0.1-2.x'
    compile 'io.reactivex.rxjava2:rxjava:2.0.5'
}
```

## Usage
Because RxSocialConnect uses RxActivityResult to deal with intent calls, all its requirements and features are inherited too.

Before attempting to use RxSocialConnect, you need to call `RxSocialConnect.register` in your Android `Application` class, supplying as parameter the current instance and an encryption key in order to save the tokens on disk encrypted, as long as an implementation of [JSONConverter](https://github.com/FuckBoilerplate/RxSocialConnect-Android/blob/master/core/src/main/java/org/fuckboilerplate/rx_social_connect/JSONConverter.java) interface.

Because RxSocialConnect uses internally [Jolyglot](https://github.com/VictorAlbertos/Jolyglot) to save on disk the tokens retrieved, you need to add one of the next dependency to gradle.

```gradle
dependencies {
    // To use Gson
    compile 'com.github.VictorAlbertos.Jolyglot:gson:0.0.3'

    // To use Jackson
    compile 'com.github.VictorAlbertos.Jolyglot:jackson:0.0.3'

    // To use Moshi
    compile 'com.github.VictorAlbertos.Jolyglot:moshi:0.0.3'
}
```

```java
public class SampleApp extends Application {

    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect.register(this, "myEncryptionKey")
            .using(new GsonSpeaker());
    }
}
```

Every feature RxSocialConnect exposes can be accessed from both, an `activity` or a `fragment` instance.

**Limitation:**: Your fragments need to extend from `android.support.v4.app.Fragment` instead of `android.app.Fragment`, otherwise they won't be notified.

The generic type of the `observable` returned by RxSocialConnect when subscribing to any of its providers is always an instance of [Response](https://github.com/FuckBoilerplate/RxSocialConnect-Android/blob/master/core/src/main/java/org/fuckboilerplate/rx_social_connect/Response.java) class.

This instance holds a reference to the current Activity/Fragment, accessible calling `targetUI()` method. Because the original one may be recreated it would be unsafe calling it. Instead, you must call any method/variable of your Activity/Fragment from this instance encapsulated in the `response` instance.

Also, this instance holds a reference to the token.

### Retrieving tokens using OAuth1.

On social networks which use OAuth1 protocol to authenticate users (such us Twitter), you need to build a [OAuth10aService](https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/oauth/OAuth10aService.java) instance and pass it to RxSocialConnect.

```java
OAuth10aService twitterService = new ServiceBuilder()
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .callback(callbackUrl)
                .build(TwitterApi.instance());

RxSocialConnect.with(fragmentOrActivity, twitterService)
                    .subscribe(response -> {
                        OAuth1AccessToken token = response.token();
                        response.targetUI().showToken(token.getToken());
                        response.targetUI().showToken(token.getTokenSecret());
                    });
```

Once the OAuth1 process has been successfully completed, you can retrieve the cached token calling `RxSocialConnect.getTokenOAuth1(defaultApi10aClass)` -where `defaultApi10aClass` is the provider `class` used on the oauth1 process.

```java
        RxSocialConnect.getTokenOAuth1(TwitterApi.class)
                .subscribe(token -> showResponse(token),
                        error -> showError(error));
```

### Retrieving tokens using OAuth2.

On social networks which use OAuth2 protocol to authenticate users (such us Facebook, Google+ or LinkedIn), you need to build a [OAuth20Service](https://github.com/scribejava/scribejava/blob/master/scribejava-core/src/main/java/com/github/scribejava/core/oauth/OAuth20Service.java) instance and pass it to RxSocialConnect.

```java
OAuth20Service facebookService = new ServiceBuilder()
                .apiKey(appId)
                .apiSecret(appSecret)
                .callback(callbackUrl)
                .scope("public_profile")
                .build(FacebookApi.instance());

RxSocialConnect.with(fragmentOrActivity, facebookService)
                    .subscribe(response -> {
                        OAuth2AccessToken token = response.token();
                        response.targetUI().showToken(token.getAccessToken());
                    });
```

Once the OAuth2 process has been successfully completed, you can retrieve the cached token calling `RxSocialConnect.getTokenOAuth2(defaultApi20Class)` -where `defaultApi20Class` is the provider `class` used on the oauth2 process.

```java
        RxSocialConnect.getTokenOAuth2(FacebookApi.class)
                .subscribe(token -> showResponse(token),
                        error -> showError(error));
```


### Token lifetime.
After retrieving the token, RxSocialConnect will save it on disk to return it on future calls without doing again the oauth process. This token only will be evicted from cache if it is a OAuth2AccessToken instance and its expiration time has been fulfilled.

But, if you need to close an specific connection (or delete the token from the disk for that matters), you can call `RxSocialConnect.closeConnection(baseApiClass)` at any time to evict the cached token -where `baseApiClass` is the provider `class` used on the oauth process.

```java
//Facebook
RxSocialConnect.closeConnection(FacebookApi.class)
                .subscribe(_I ->  showToast("Facebook disconnected"));

//Twitter
RxSocialConnect.closeConnection(TwitterApi.class)
                .subscribe(_I ->  showToast("Twitter disconnected"));
```

You can also close all the connections at once, calling `RxSocialConnect.closeConnections()`

```java
RxSocialConnect.closeConnections()
                .subscribe(_I ->  showToast("All disconnected"));
```


### OkHttp interceptors.
RxSocialConnect can be powered with [OkHttp](https://github.com/square/okhttp) (or [Retrofit](https://github.com/square/retrofit) for that matters) to bypass authentication header configuration when dealing with specific endpoints.
Using the interceptors provided by RxSocialConnect, it's a 0 configuration process to be able to reach any http resource from any api client (Facebook, Twitter, etc).

First of all, install RxSocialConnectInterceptors library using gradle:

```gradle
dependencies {
    compile 'com.github.VictorAlbertos.RxSocialConnect-Android:okhttp_interceptors:1.0.1-2.x'
}
```

After you have retrieved a valid token -if you attempt to use these interceptors prior to retrieving a valid token a [NotActiveTokenFoundException](https://github.com/FuckBoilerplate/RxSocialConnect-Android/blob/master/core/src/main/java/org/fuckboilerplate/rx_social_connect/NotActiveTokenFoundException.java) will be thrown, you can now use [OAuth1Interceptor](https://github.com/FuckBoilerplate/RxSocialConnect-Android/blob/master/okhttp_interceptors/src/main/java/io/victoralbertos/rx_social_connect/OAuth1Interceptor.java) or [OAuth2Interceptor](https://github.com/FuckBoilerplate/RxSocialConnect-Android/blob/master/okhttp_interceptors/src/main/java/io/victoralbertos/rx_social_connect/OAuth2Interceptor.java) classes to bypass the authentication headers configuration, depending on the OAuth version of your social network of interest.

#### OAuth1Interceptor.
```java
OAuth10aService yahooService = //...

OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OAuth1Interceptor(yahooService))
                .build();

//If using retrofit...
YahooApiRest yahooApiRest = new Retrofit.Builder()
        .baseUrl("")
        .client(client)
        .build().create(YahooApiRest.class);
```

#### OAuth2Interceptor.
```java
OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OAuth2Interceptor(FacebookApi.class))
                .build();

//If using retrofit...
FacebookApiRest facebookApiRest = new Retrofit.Builder()
        .baseUrl("")
        .client(client)
        .build().create(FacebookApiRest.class);
```

Now you are ready to perform any http call against any api in the same way you would do it for no OAuth apis.

## Examples
* Social networks connections examples can be found [here](https://github.com/FuckBoilerplate/RxSocialConnect-Android/tree/master/app/src/main/java/org/fuckboilerplate/rxsocialconnect/connections).
* OkHttp interceptors examples can be found [here](https://github.com/FuckBoilerplate/RxSocialConnect-Android/tree/master/app/src/main/java/org/fuckboilerplate/rxsocialconnect/interceptors).

## Proguard
```
-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn org.apache.commons.codec.**
-dontwarn com.ning.http.client.**

keep class org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth1AccessToken {
    <fields>;
}
-keep class org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth2AccessToken {
    <fields>;
}
```

## Credits
* Oauth core authentication: [ScribeJava](https://github.com/scribejava/scribejava)

## Author
**Víctor Albertos**
* <https://twitter.com/_victorAlbertos>
* <https://www.linkedin.com/in/victoralbertos>
* <https://github.com/VictorAlbertos>

## Another author's libraries using RxJava:
* [RxCache](https://github.com/VictorAlbertos/RxCache): Reactive caching library for Android and Java.
* [Mockery](https://github.com/VictorAlbertos/Mockery): Android and Java library for mocking and testing networking layers with built-in support for Retrofit
* [RxActivityResult](https://github.com/VictorAlbertos/RxActivityResult): A reactive-tiny-badass-vindictive library to break with the OnActivityResult implementation as it breaks the observables chain.
* [RxFcm](https://github.com/VictorAlbertos/RxFcm): RxJava extension for Android Firebase Cloud Messaging (aka fcm).
* [RxPaparazzo](https://github.com/FuckBoilerplate/RxPaparazzo): RxJava extension for Android to take images using camera and gallery.
