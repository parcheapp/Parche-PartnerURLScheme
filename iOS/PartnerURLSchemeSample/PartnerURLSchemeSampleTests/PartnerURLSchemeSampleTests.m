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


static NSString * const FakeAPIKey = @"FAKE_API_KEY";
static NSString * const FakeDiscountCode = @"DISCOUNT_CODE";
static NSString * const FakeStandardUserID = @"USER_ID";
static NSString * const ExpectedNoDiscountURLString = @"goparche://open?api_key=FAKE_API_KEY";
static NSString * const ExpectedStandardDiscountURLString = @"goparche://open?partner_user_id=USER_ID&discount_code=DISCOUNT_CODE&api_key=FAKE_API_KEY";

#import "PARPartnerURLSchemeHelper+Testing.h"

@interface PartnerURLSchemeSampleTests : XCTestCase
@property (nonatomic, strong) UIApplication *mockApplication;
@end

@implementation PartnerURLSchemeSampleTests

- (void)setUp
{
    [super setUp];
    
    //Setup the mock and then assign it to the shared scheme helper.
    self.mockApplication = mock([UIApplication class]);
    [PARPartnerURLSchemeHelper setApplicaitonForTesting:self.mockApplication];
}

- (void)tearDown
{
    //Put it all back the way it was. 
    [PARPartnerURLSchemeHelper setApplicaitonForTesting:nil];
    self.mockApplication = nil;
    
    [super tearDown];
}

#pragma mark - OCMockito readability helpers

- (void)setMockCanOpenParche:(BOOL)canOpen
{
    [given([self.mockApplication canOpenURL:[NSURL URLWithString:@"goparche://open"]]) willReturnBool:canOpen];
}

- (void)verifyMockOpenedURL:(NSString *)urlString
{
    [verifyCount(self.mockApplication, times(1)) openURL:[NSURL URLWithString:urlString]];
}

- (void)verifyMockNeverOpenedURL:(NSString *)urlString
{
    [verifyCount(self.mockApplication, never()) openURL:[NSURL URLWithString:urlString]];
}

#pragma mark - Actual Tests

- (void)testIfApplicationCannotBeOpenedWeShouldInstallOrUpgrade
{
    [self setMockCanOpenParche:NO];
    BOOL shouldInstallOrUpgrade = [PARPartnerURLSchemeHelper parcheNeedsToBeUpdatedOrInstalled];
    XCTAssertTrue(shouldInstallOrUpgrade);
}

- (void)testIfApplicationCanBeOpenedWeDontNeedToInstallOrUpgrade
{
    [self setMockCanOpenParche:YES];
    BOOL shouldInstallOrUpgrade = [PARPartnerURLSchemeHelper parcheNeedsToBeUpdatedOrInstalled];
    XCTAssertFalse(shouldInstallOrUpgrade);
}

- (void)testCallingAppStoreOpenWouldCallTheCorrectURL
{
    [PARPartnerURLSchemeHelper showParcheInAppStore];
    [self verifyMockOpenedURL:ParcheAppStoreURL];
}

- (void)testOpeningWithoutDiscountShouldWork
{
    [self setMockCanOpenParche:YES];
    BOOL canOpen = [PARPartnerURLSchemeHelper openParcheWithAPIKey:FakeAPIKey];
    XCTAssertTrue(canOpen);
    [self verifyMockOpenedURL:ExpectedNoDiscountURLString];
}

- (void)testOpeningWithoutDiscountShouldFailIfAppNeedsInstallOrUpgrade
{
    [self setMockCanOpenParche:NO];
    BOOL canOpen = [PARPartnerURLSchemeHelper openParcheWithAPIKey:FakeAPIKey];
    XCTAssertFalse(canOpen);
    [self verifyMockNeverOpenedURL:ExpectedNoDiscountURLString];
}

- (void)testOpeningWithDiscountShouldWork
{
    [self setMockCanOpenParche:YES];
    BOOL canOpen = [PARPartnerURLSchemeHelper openParcheAndRequestDiscountForUser:FakeStandardUserID
                                                                     discountCode:FakeDiscountCode
                                                                           apiKey:FakeAPIKey];
    XCTAssertTrue(canOpen);
    [self verifyMockOpenedURL:ExpectedStandardDiscountURLString];
}

- (void)testOpeningWithDiscountShouldFailIfAppNeedsInstallOrUpgrade
{
    [self setMockCanOpenParche:NO];
    BOOL canOpen = [PARPartnerURLSchemeHelper openParcheAndRequestDiscountForUser:FakeStandardUserID
                                                                     discountCode:FakeDiscountCode
                                                                           apiKey:FakeAPIKey];
    XCTAssertFalse(canOpen);
    [self verifyMockNeverOpenedURL:ExpectedStandardDiscountURLString];
}

- (void)testOpeningWithDiscountAndUserIdThatNeedsEncodingShouldWork
{
    [self setMockCanOpenParche:YES];
    BOOL canOpen = [PARPartnerURLSchemeHelper openParcheAndRequestDiscountForUser:@"USER ID"
                                                                     discountCode:FakeDiscountCode
                                                                           apiKey:FakeAPIKey];
    XCTAssertTrue(canOpen);
    [self verifyMockOpenedURL:@"goparche://open?partner_user_id=USER%20ID&discount_code=DISCOUNT_CODE&api_key=FAKE_API_KEY"];
}

@end
