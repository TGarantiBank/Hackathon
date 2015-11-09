## Android Studio

You can find HackathonSample app under [Android folder](https://github.com/bleshinc/GarantiHackathon/Android) we've created for this hackathon. It's an empty project showing you how to integrate Blesh SDK into your own project. Using the same project, you can import the Blesh SDK module inside sample project and implement the necessary starting code blocks into your own application for convenience.

#### Manually importing the .aar file

After importing Blesh SDK module, modify build.gradle of your application as shown below;

```
dependencies {
    compile 'com.google.code.gson:gson:2.3.1'
    compile 'com.squareup.okhttp:okhttp:2.4.0'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.4'
    compile 'com.google.android.gms:play-services-location:8.1.0'
}

```

If you encounter [this](https://github.com/square/okhttp/issues/1346) issue add the following option in build.gradle of your application module;

```
android {
    lintOptions {
        abortOnError false
    }
}
```

### Start

We recommend implementing the following function and using it with your provided credentials.

* Keep in mind that optionalKey is optional while integrationId is not, both fields accept only String objects.
* Please also make sure your registered bundle/package name is correct. (ie:com.my.application)
* integrationId - some unique id in case you'd prefer to customize your content for your users

```
public void startBlesh(String apiUser, String apiKey, String integrationId) {
        startService(new BleshIntent.Builder(apiUser, apiKey, integrationId).optionalKey("M").getIntent(this));
}

```

_Now you can call this function from anywhere within your application context, please note that some parameters are optional._

### Receiving Local Notifications

```
// Define a callback reference to be used by the Blesh service
// in order to push the user's action results to your application
BleshTemplateResult result = new BleshTemplateResult() {
    @Override
    public void bleshTemplateResultCallback(String actionType,
                                            String actionValue) {
        if (actionType != null && actionValue != null) {
            Log.i(TAG, "I received type:" + actionType + " value:"
                    + actionValue);
            // Check for the action type and value you want to use
            // You may wish to load a web page here using the action value
        } else {
            Log.w(TAG, "bleshTemplateResultCallback() result is empty!");
        }
    }
};

// Register your callback function, named as "result" for this example
BleshInstance.sharedInstance().setTemplateResult(result);
```