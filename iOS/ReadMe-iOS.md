#Parche URL Scheme Helper: iOS

The files which you will be using in your application are the [PartnerURLSchemeHelper](PartnerURLSchemeSample/PartnerURLSchemeHelper) files. 

#Installation

We strongly recommend using [CocoaPods](http://cocoapods.org/) to add these files as a dependency. To add the files, add the following line to your `Podfile`:

```ruby
pod 'ParchePartnerURLScheme', '~> 1.0'
```

And then running `pod install`. We also recommend using Orta Therox's [cocoapods-keys](https://github.com/orta/cocoapods-keys/) plugin to secure your API key. 

#PARPartnerURLSchemeHelper

There are four public class methods on the helper class. 

These are more extensively documented inline (and the inline documentation should be considered canonical), but in brief: 

- `+ (BOOL)parcheNeedsToBeUpdatedOrInstalled` tells your application whether the user needs to install or update Parche.
- `+ (void)showParcheInAppStore` kicks the user to the App Store to install or update Parche. 
- `+ (BOOL)openParcheWithAPIKey:(NSString *)apiKey` opens Parche without applying a discount, but identifies the app opening Parche.
- `+ (BOOL)openParcheAndRequestDiscountForUser:(NSString *)partnerUserID discountCode:(NSString *)discountCode apiKey:(NSString *)apiKey` opens the application and passes in the information provided in the parameters, giving the user the ability to use the provided discount code after they login or register with Parche. 