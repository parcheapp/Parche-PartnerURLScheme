//
//  PARPartnerURLSchemeHelper.m
//  PartnerURLSchemeSample
//
//  Created by Ellen Shapiro (Vokal) on 3/3/15.
//  Copyright (c) 2015 Parche. All rights reserved.
//

#import "PARPartnerURLSchemeHelper.h"

static NSString * const ParcheURLScheme = @"goparche://";
static NSString * const ParcheOpenEndpoint = @"open";
static NSString * const ParcheNoDiscountFormat = @"?api_key=%@";
static NSString * const ParcheDiscountFormat = @"?partner_user_id=%@&discount_code=%@&api_key=%@";
static NSString * const ParcheAppStoreURL = @"https://itunes.apple.com/us/app/parche-valet-without-delay/id943516663?ct=partner";

@interface PARPartnerURLSchemeHelper()
@property (nonatomic) UIApplication *application;
@end

@implementation PARPartnerURLSchemeHelper

+ (void)setApplicaitonForTesting:(UIApplication *)application
{
    [self sharedInstance].application = application;
}

+ (PARPartnerURLSchemeHelper *)sharedInstance
{
    static PARPartnerURLSchemeHelper *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
        sharedInstance.application = [UIApplication sharedApplication];
    });

    return sharedInstance;
}

+ (NSString *)openAppURLString
{
    return [ParcheURLScheme stringByAppendingString:ParcheOpenEndpoint];
}

+ (BOOL)parcheNeedsToBeUpdatedOrInstalled
{
    return ![[self sharedInstance].application canOpenURL:[NSURL URLWithString:[self openAppURLString]]];
}

+ (void)showParcheInAppStore
{
    [[self sharedInstance].application openURL:[NSURL URLWithString:ParcheAppStoreURL]];
}

+ (BOOL)openParcheWithAPIKey:(NSString *)apiKey
{
    if (![self parcheNeedsToBeUpdatedOrInstalled]) {
        NSString *urlString = [[self openAppURLString] stringByAppendingFormat:ParcheNoDiscountFormat, apiKey];
        [[self sharedInstance].application openURL:[NSURL URLWithString:urlString]];
        return YES;
    } else {
        return NO;
    }
}

+ (BOOL)openParcheAndRequestDiscountForUser:(NSString *)partnerUserID
                               discountCode:(NSString *)discountCode
                                     apiKey:(NSString *)apiKey
{
    if (![self parcheNeedsToBeUpdatedOrInstalled]) {
        NSString *encodedUserID = [partnerUserID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSString *path = [NSString stringWithFormat:ParcheDiscountFormat, encodedUserID, discountCode, apiKey];
        NSString *urlString = [[self openAppURLString] stringByAppendingString:path];
        [[self sharedInstance].application openURL:[NSURL URLWithString:urlString]];
        return YES;
    } else {
        return NO;
    }
}

@end
