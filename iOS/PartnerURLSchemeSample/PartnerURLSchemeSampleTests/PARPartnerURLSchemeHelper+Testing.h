//
//  PARPartnerURLSchemeHelper+Testing.h
//  PartnerURLSchemeSample
//
//  Created by Ellen Shapiro (Vokal) on 3/3/15.
//  Copyright (c) 2015 Parche. All rights reserved.
//

#import "PARPartnerURLSchemeHelper.h"

/**
 * Helper to make test-only methods public.
 */
@interface PARPartnerURLSchemeHelper (Testing)

/**
 * Allows the developer to set a mock application for testing.
 */
+ (void)setApplicaitonForTesting:(UIApplication *)application;

@end
