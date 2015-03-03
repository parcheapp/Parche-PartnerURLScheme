//
//  PARPartnerURLSchemeHelper.h
//  PartnerURLSchemeSample
//
//  Created by Ellen Shapiro (Vokal) on 3/3/15.
//  Copyright (c) 2015 Parche. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 *  Helper class to assist in calling Parche's URL scheme.
 */
@interface PARPartnerURLSchemeHelper : NSObject

/**
 *  Determines if there is a version of Parche on the user's device which responds to 
 *  this URL scheme.
 *
 *  @return YES if the application needs to be updated or installed, NO if you are clear to
 *          go ahead and call the request discount method.
 */
+ (BOOL)parcheNeedsToBeUpdatedOrInstalled;

/**
 *  Opens the App store to show the Parche application. If the user does not have the app
 *  installed, they will see "Get".
 *
 *  If the user has Parche installed, they will see "Open" if there is no pending update or
 *  "Update" if there is a pending update.
 */
+ (void)showParcheInAppStore;

/**
 *  Opens the Parche Application without a discount, but indicating what app the request is coming from.
 *
 *  @param apiKey The partner application's Parche API key.
 *
 *  @return YES if the request opened the Parche application successfully, NO if it did not.
 */
+ (BOOL)openParcheWithAPIKey:(NSString *)apiKey;

/**
 *  Opens the Parche application and passes the required information along to it.
 *
 *  @param partnerUserID The user ID used to identify the current user on the partner application's end. 
                         NOTE: Will be URL encoded by this class, DO NOT URL ENCODE before passing in. 
 *  @param discountCode  The discount code retrieved from the Parche server.
 *  @param apiKey        The partner application's Parche API Key. 
 *
 *  @return YES if the request opened the Parche application successfully, NO if it did not.
 */
+ (BOOL)openParcheAndRequestDiscountForUser:(NSString *)partnerUserID
                               discountCode:(NSString *)discountCode
                                     apiKey:(NSString *)apiKey;

@end
