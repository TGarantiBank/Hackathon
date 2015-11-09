## Implementation Guide

[Download Blesh iOS SDK](https://www.dropbox.com/s/t3uo36p23fi1z3f/BleshSDKv2_0_0_2.zip?dl=0)

### Importing Blesh SDK Framework in your Xcode project

Import BleshSDK.framework in your Xcode project. Find the reference image below.

![BleshSDK.framework](https://www.blesh.com/wp-content/uploads/2014/09/1.png)

### Other Frameworks

Other required frameworks are below. You need to import those to your project, as well.

> Accelerate.framework, AudioToolBox.framework, CoreLocation.framework, CoreBluetooth.framework, CoreTelephony.framework and SystemConfiguration.framework.

Find the reference image below.

![others](https://www.blesh.com/wp-content/uploads/2014/09/bleshframeworks.png)

## Configuring Info.plist

To provide best UX for your subscribers you need to have some permissions after your application is installed. Per iOS User guides apps can ask for required permissions with their own sentences. These configurations can be tuned under Info.plist folder.

You can, of course, get more privileges, but NSLocationAlwaysUsage permission is required for best iBeacon UX. If, either you don’t get or subscriber does not give this permission, your app does not get notified when the app is in background or not in working state.

**Info.plist**

**Key:** NSLocationAlwaysUsageDescription
**Value:** “Your app requires this permission for serving with the best results”

## Blesh Notification Sound
Include BleshNotification.caf in your resources. It will appear as below.

![sound](https://www.blesh.com/wp-content/uploads/2014/09/Ads%C4%B1z-2.png)

## Defining Appdelegate

Lets do some coding now.

BleshSDK needs to be defined in AppDelegate as shown below. This also helps your application to manage local notifications.

```
#import <BleshSDK/Blesh.h>

-(BOOL) application: (UIApplication*)application
         didFinishLaunchingWithOptions: (NSDictionary*)launchOptions
{

    UILocalNotification* notification =[launchOptions
    objectForKey:UIApplicationLaunchOptionsLocalNotificationKey];

    if(notification){
        [[Blesh sharedInstance] bleshReceivedLocalNotification:notification];
    }
    return YES;
}

-(void) application: (UIApplication*)application
         didReceiveLocalNotification: (UILocalNotification*)notification {
    [[Blesh sharedInstance] bleshReceivedLocalNotification:notification];
}

-(void) application: (UIApplication*)application
         handleActionWithIdentifier: (NSString*)identifier
               forLocalNotification: (UILocalNotification*)notification
                  completionHandler: (void(^)())completionHandler{

    [[Blesh sharedInstance] bleshReceivedLocalNotification:notification];
    completionHandler();
}
```

## Initializing Blesh SDK

Now you can give information about your subscriber to the platform.

This step involves initializing the BleshSDK in your project once the user credentials are received. If you do not have an APIUser and an APIKey please sign-in to Blesh CMS Platform. APIUser and APIKey are shown next to application definition in your account.

Integration Type needs to be “M”. IntegrationId field is for the unique subscriber token. You can give any information which makes the subscriber unique in your application’s understanding.

```
[[Blesh sharedInstance] initBleshWithAPIUser:@"APIUser"
                                      APIKey:@"APIKey"
                              integrationType:@"M"
                                integrationId:@"integrationId"
                                    pushToken:@"Push Notification Token"
                                  optionalKey:@"Optional Key"];
```

## Receiving User Interactions

Once the user receives the push notification and opens it; BleshSDK informs your app that the user has interacted with your content. In order to receive this information, implement the code block below to your app.

```
[[Blesh sharedInstance] setDidCloseCampaignView:^(NSString* valueType,NSString* value){}];
```

That’s all. Now you can assign a campaign using Blesh CMS Platform – My Campaigns section.

If you have an iBeacon, you can also experience the iBeacon campaign from your application. In order to do that, don’t forget to mount your iBeacon with corresponding UUID, Major and Minor values to the Blesh CMS Platform.

Can’t get enough? Try [Community](http://community.blesh.com/) pages for further information or pro tips.
