#Parche URL Scheme Helper: Android

The file which you will be using in your application is the [ParchePartnerURLSchemeHelper](PartnerURLSchemeSample/app/src/main/java/com/parche/partnerurlschemesample/ParchePartnerURLSchemeHelper.java) java file. 

##Installation

This class can be added to your project using Gradle. Add the following line to the `dependencies` closure in your `build.gradle` file at the app level: 

```groovy
compile 'TODO'
```

#ParchePartnerURLSchemeHelper

There are four public class methods on the helper class. 

These are more extensively documented inline (and the inline documentation should be considered canonical), but in brief: 

- `parcheNeedsToBeUpdatedOrInstalled(Context aContext)` tells your application whether the user needs to install or update Parche.
- `showParcheInPlayStoreIntent(Context aContext)` returns an [Intent](http://developer.android.com/reference/android/content/Intent.html) which kicks the user to the App Store to install or update Parche. 
- `openParcheIntent(Context aContext, String aAPIKey)` returns an [Intent](http://developer.android.com/reference/android/content/Intent.html) which opens Parche without applying a discount, but identifies the app opening Parche.
- `openParcheAndRequestDiscount(Context aContext, String aDiscountCode, String aPartnerUserID, String aAPIKey)` returns an [Intent](http://developer.android.com/reference/android/content/Intent.html) which opens Parche and passes in the information provided in the parameters, giving the user the ability to use the provided discount code after they login or register with Parche. 

Note that the calling application is responsible for actually starting activities based on the returned `Intents`. 

