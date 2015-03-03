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
    [PARPartnerURLSchemeHelper showParcheInAppStore];
}

@end
