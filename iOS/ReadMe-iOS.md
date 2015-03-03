#Parche URL Scheme Helper: iOS

The files which you should include in your application are available in the [PartnerURLSchemeHelper](PartnerURLSchemeSample/PartnerURLSchemeHelper) folder within the sample project. 

Copy the folder and all of its contents into your Xcode project, adding the files to your application target (or targets). 

#PARPartnerURLSchemeHelper

There are four public class methods on the helper class. 

These are more extensively documented inline (and the inline documentation should be considered canonical), but in brief: 

- `+ (BOOL)parcheNeedsToBeUpdatedOrInstalled` tells your application whether the user needs to install or update Parche.
- `+ (void)showParcheInAppStore` kicks the user to the App Store to install or update Parche. 
- `+ (BOOL)openParcheWithAPIKey:(NSString *)apiKey` opens Parche without applying a discount, but identifies the app opening Parche.
- `+ (BOOL)openParcheAndRequestDiscountForUser:(NSString *)partnerUserID discountCode:(NSString *)discountCode apiKey:(NSString *)apiKey` opens the application and passes in the information provided in the parameters, giving the user the ability to use the provided discount code after they login or register with Parche. 