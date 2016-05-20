OAuth RxJava extension for Android. 

# RxSocialConnect

RxSocialConnect simplifies the process of retrieving authorizations tokens from multiple social networks to a minimalist observable call, from any Fragment or Activity. 

```java
OAuth20Service facebookService = //...

RxSocialConnect.with(fragmentOrActivity, facebookService)
                    .subscribe(response -> response.targetUI().showResponse(response.token()));
```

## Features:

* Webview implementation to handle the sequent steps of oauth process.
* Storage tokens locally.
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
    compile "com.github.FuckBoilerplate:RxSocialConnect:0.0.1"
    compile "io.reactivex:rxjava:1.1.5"
}
```

## Usage
Because RxSocialConnect uses RxActivityResult to deal with intent calls, all its requirements and features are inherited too.

Before attempting to use RxSocialConnect, you need to call `RxSocialConnect.register` in your Android `Application` class, supplying as parameter the current instance.
        
```java
public class SampleApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        RxSocialConnect.register(this);
    }
}
```

Every feature RxPaparazzo exposes can be accessed from both, an `activity` or a `fragment` instance. 

**Limitation:**: Your fragments need to extend from `android.support.v4.app.Fragment` instead of `android.app.Fragment`, otherwise they won't be notified. 

The generic type of the `observable` returned by RxSocialConnect when subscribing to any of its providers is always an instance of [Response]() class. 

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
 

### Token lifetime.
After retrieving the token, RxSocialConnect will save it on disk to return it on future calls without doing again the oauth process. This token only will be evicted from cache if it is a OAuth2AccessToken instance and its expiration time has been fulfilled. 

But, if you need to close an specific connection (or delete the token from the disk for that matters), you can call `RxSocialConnect.closeConnection(context, baseApiClass)` at any time to evict the cached token -where `baseApiClass` is the provider `class` used on the oauth process. 

```java
//Facebook
RxSocialConnect.closeConnection(context, FacebookApi.class)
                .subscribe(_I ->  showToast("Facebook disconnected"));

//Twitter
RxSocialConnect.closeConnection(context, TwitterApi.class)
                .subscribe(_I ->  showToast("Twitter disconnected"));
```

You can also close all the connections at once, calling `RxSocialConnect.closeConnections(context)`

```java
RxSocialConnect.closeConnections(context)
                .subscribe(_I ->  showToast("All disconnected"));
```

## Credits
* Oauth core authentication: [ScribeJava](https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples)

## Author

**Víctor Albertos**
* <https://twitter.com/_victorAlbertos>
* <https://www.linkedin.com/in/victoralbertos>
* <https://github.com/VictorAlbertos>

## Another author's libraries using RxJava:
* [RxCache](https://github.com/VictorAlbertos/RxCache): Reactive caching library for Android and Java. 
* [RxGcm](https://github.com/VictorAlbertos/RxGcm): RxJava extension for Gcm which acts as an architectural approach to easily satisfy the requirements of an android app when dealing with push notifications.
* [RxPaparazzo](https://github.com/FuckBoilerplate/RxPaparazzo): RxJava extension for Android to take images using camera and gallery.
* [RxActivityResult](https://github.com/VictorAlbertos/RxActivityResult): A reactive-tiny-badass-vindictive library to break with the OnActivityResult implementation as it breaks the observables chain. 