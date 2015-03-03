//
//  PartnerURLSchemeSampleTests.m
//  PartnerURLSchemeSampleTests
//
//  Created by Ellen Shapiro (Vokal) on 3/3/15.
//  Copyright (c) 2015 Parche. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

//OCMockito stuff:

#define HC_SHORTHAND
#import <OCHamcrestIOS/OCHamcrestIOS.h>

#define MOCKITO_SHORTHAND
#import <OCMockitoIOS/OCMockitoIOS.h>


#import "PARPartnerURLSchemeHelper+Testing.h"

@interface PartnerURLSchemeSampleTests : XCTestCase
@property (nonatomic, strong) UIApplication *mockApplication;
@end

@implementation PartnerURLSchemeSampleTests

- (void)setUp
{
    [super setUp];
    
    self.mockApplication = mock([UIApplication class]);
    [PARPartnerURLSchemeHelper setApplicaitonForTesting:self.mockApplication];
}

- (void)tearDown
{
    self.mockApplication = nil;
    [super tearDown];
}

- (void)testIfApplicationCannotBeOpenedWeShouldInstallOrUpgrade
{
    [given([self.mockApplication canOpenURL:[NSURL URLWithString:@"goparche://open"]]) willReturnBool:NO];
    BOOL shouldInstallOrUpgrade = [PARPartnerURLSchemeHelper parcheNeedsToBeUpdatedOrInstalled];
    XCTAssertTrue(shouldInstallOrUpgrade);
}

- (void)testIfApplicationCanBeOpenedWeDontNeedToInstallOrUpgrade
{
    [given([self.mockApplication canOpenURL:[NSURL URLWithString:@"goparche://open"]]) willReturnBool:YES];
    BOOL shouldInstallOrUpgrade = [PARPartnerURLSchemeHelper parcheNeedsToBeUpdatedOrInstalled];
    XCTAssertFalse(shouldInstallOrUpgrade);
}

@end
