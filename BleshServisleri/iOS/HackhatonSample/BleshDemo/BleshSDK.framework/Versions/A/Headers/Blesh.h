//
//  Blesh.h
//  BleshSDK
//
//  Created by Blesh on 27.12.2013.
//  Copyright (c) 2013 Blesh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>
#import <CoreBluetooth/CoreBluetooth.h>
#import "BleshDevice.h"

@interface Blesh : NSObject <CLLocationManagerDelegate,UIApplicationDelegate, CBPeripheralManagerDelegate>

+ (Blesh *) sharedInstance;

/**
 *  Blesh SDK gives the information that customer clicked on the notification. If the actionValue = null means that the customer closed the notification via close 
 *  button on the corner of the screen
 */
@property (nonatomic, copy) void (^didCloseCampaignView)(NSString * actionValueType, NSString * actionValue);
@property (nonatomic, copy) void (^didRangeBleshDevice)(NSMutableArray * deviceList);


/**
 *  This method initializes Blesh SDK.
 *
 *  @param APIUser         Username for to initialize Blesh
 *  @param APIKey          Password for to initialize Blesh
 *  @param integrationType integration type stands for describing integrationId type
 *                         M = native integrationId type
 *                         F = Facebook access token
 *  @param integrationId   unique customerId which will be transferred via BleshAPI
 *  @param pushToken       APNS Push token
 */

- (void) initBleshWithAPIUser:(NSString *) APIUser
                       APIKey:(NSString *) APIKey
              integrationType:(NSString *) integrationType
                integrationId:(NSString *) integrationId
                    pushToken:(NSString *) pushToken
                  optionalKey:(NSString *) optionalKey;

- (void) bleshReceivedLocalNotification:(UILocalNotification *) notification;
- (void) bleshPerformFetchWithCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;
@end
