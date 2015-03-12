//
//  ViewController.m
//  PartnerURLSchemeSample
//
//  Created by Ellen Shapiro (Vokal) on 3/3/15.
//  Copyright (c) 2015 Parche. All rights reserved.
//

#import "ViewController.h"

#import "PARPartnerURLSchemeHelper.h"

@interface ViewController ()
@property (nonatomic, weak) IBOutlet UILabel *canOpenLabel;

@end

@implementation ViewController

#pragma mark - View Lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self checkParcheNeedsUpdateOrInstall];
}

#pragma mark - IBActions

- (IBAction)checkParcheNeedsUpdateOrInstall
{
    BOOL needs = [PARPartnerURLSchemeHelper parcheNeedsToBeUpdatedOrInstalled];
    self.canOpenLabel.text = needs ? @"YES" : @"NO";
}

- (IBAction)showParcheInAppStore
{
#if TARGET_IPHONE_SIMULATOR
    [self showSimpleAlertVCWithTitle:@"Simulator!" message:@"The app store link doesn't work on the simulator. Please test on a device."];
#else
    [PARPartnerURLSchemeHelper showParcheInAppStore];
#endif
}

- (IBAction)openAppWithoutDiscount
{
    if (![PARPartnerURLSchemeHelper openParcheWithAPIKey:@"YOUR_API_KEY"]) {
        //NOTE: Use NSLocalizedString in anything that actually faces your users.
        [self showSimpleAlertVCWithTitle:@"Ruh Roh!" message:@"You do not have a version of Parche which supports the URL scheme installed."];
    }
}

- (IBAction)openAppWithFakeDiscount
{
    if (![PARPartnerURLSchemeHelper openParcheAndRequestDiscountForUser:@"Partner User ID" discountCode:@"DISCOUNT_CODE" apiKey:@"FAKE_API_KEY"]) {
        [self showSimpleAlertVCWithTitle:@"Ruh Roh!" message:@"You do not have a version of Parche which supports the URL scheme installed."];
    }
}

- (void)showSimpleAlertVCWithTitle:(NSString *)title message:(NSString *)message
{
    UIAlertController *alertVC = [UIAlertController alertControllerWithTitle:title
                                                                     message:message
                                                              preferredStyle:UIAlertControllerStyleAlert];
    
    typeof(self) __weak weakSelf = self;
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK"
                                                 style:UIAlertActionStyleDefault
                                               handler:^(UIAlertAction *action) {
                                                   [weakSelf dismissViewControllerAnimated:YES completion:nil];
                                               }];
    
    [alertVC addAction:ok];
    [self presentViewController:alertVC
                       animated:YES
                     completion:nil];
}

@end
