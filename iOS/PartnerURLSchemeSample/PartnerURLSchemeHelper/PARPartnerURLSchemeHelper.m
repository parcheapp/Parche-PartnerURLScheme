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
static NSString * const ParcheOpenFormat = @"open?partner_user_id=%@&discount_code=%@&api_key=%@";
static NSString * const ParcheAppStoreURL = @"https://itunes.apple.com/us/app/parche-valet-without-delay/id943516663?ct=partner";

@implementation PARPartnerURLSchemeHelper

+ (NSURL *)openWithoutDiscountURL
{
    NSString *urlString = [ParcheURLScheme stringByAppendingString:ParcheOpenEndpoint];
    return [NSURL URLWithString:urlString];
}

+ (BOOL)parcheNeedsToBeUpdatedOrInstalled
{
    return ![[UIApplication sharedApplication] canOpenURL:[self openWithoutDiscountURL]];
}

+ (void)showParcheInAppStore
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:ParcheAppStoreURL]];
}

+ (BOOL)openWithoutDiscount
{
    if (![self parcheNeedsToBeUpdatedOrInstalled]) {
        [[UIApplication sharedApplication] openURL:[self openWithoutDiscountURL]];
        return YES;
    } else {
        return NO;
    }
}

+ (BOOL)requestDiscountForUser:(NSString *)partnerUserID
                      withCode:(NSString *)discountCode
                        apiKey:(NSString *)apiKey
{
    if (![self parcheNeedsToBeUpdatedOrInstalled]) {
        
        NSString *path = [NSString stringWithFormat:ParcheOpenFormat, partnerUserID, discountCode, apiKey];
        NSString *urlString = [ParcheURLScheme stringByAppendingString:path];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlString]];        
        return YES;
    } else {
        return NO;
    }
}


@end
