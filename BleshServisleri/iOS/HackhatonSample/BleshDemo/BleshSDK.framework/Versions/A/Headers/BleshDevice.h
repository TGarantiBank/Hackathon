//
//  BleshDevice.h
//  BleshSDK
//
//  Created by skizilkaya on 13.05.2015.
//  Copyright (c) 2015 Blesh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

@interface BleshDevice : NSObject
@property (nonatomic, assign) CLProximity    proximity;
@property (nonatomic, assign) NSString     * proximityString;
@property (nonatomic, assign) NSInteger      rssi;
@property (nonatomic, strong) NSString     * deviceSerial;
@end
