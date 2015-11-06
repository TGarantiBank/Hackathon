//
//  AppDelegate.m
//  BleshDemo
//
//  Created by skizilkaya on 28.02.2014.
//  Copyright (c) 2014 blesh. All rights reserved.
//
 
#import "AppDelegate.h"
#import <BleshSDK/Blesh.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    UILocalNotification *localNotification = launchOptions[UIApplicationLaunchOptionsLocalNotificationKey];
    if (localNotification) {
        [self application:application didReceiveLocalNotification:localNotification];
    }
    
    NSLog(@"App Delegate calisiyor..........");
    
    [[Blesh sharedInstance] initBleshWithAPIUser:@"APIUser"
                                          APIKey:@"APIKey"
                                 integrationType:@"M"
                                   integrationId:@"IntegrationId"
                                       pushToken:@"Optional"
                                     optionalKey:@"Optional"];
    
    [[Blesh sharedInstance] setDidCloseCampaignView:^(NSString *key , NSString * val) {
        
    }];
    
    [[Blesh sharedInstance] setDidRangeBleshDevice:^(NSMutableArray * deviceList) {
        for (BleshDevice * device in deviceList) {
            NSLog(@"%@", device);
        }
        
        NSLog(@"------------------------------------------");
    }];
    
    return YES;
}
							
- (void) application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
    NSLog(@"Notification calisiyor.");
    [[Blesh sharedInstance] bleshReceivedLocalNotification:notification];
}

- (void) applicationWillTerminate:(UIApplication *)application {
}

- (void) application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
   completionHandler:(void(^)())completionHandler{
    [[Blesh sharedInstance] bleshReceivedLocalNotification:notification];
    completionHandler();
    
}



@end
