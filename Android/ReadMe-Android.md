#Parche URL Scheme Helper: Android

The file which you should include in your application is the [ParchePartnerURLSchemeHelper](PartnerURLSchemeSample/app/src/main/java/com/parche/partnerurlschemesample/ParchePartnerURLSchemeHelper.java) java file. Add that file to your project. 

#ParchePartnerURLSchemeHelper

There are four public class methods on the helper class. 

These are more extensively documented inline (and the inline documentation should be considered canonical), but in brief: 

- `parcheNeedsToBeUpdatedOrInstalled(Context aContext)` tells your application whether the user needs to install or update Parche.
- `showParcheInPlayStore(Context aContext)` kicks the user to the App Store to install or update Parche. 
- `openParche(Context aContext, String aAPIKey)` opens Parche without applying a discount, but identifies the app opening Parche.
- `openParcheAndRequestDiscount(Context aContext, String aDiscountCode, String aPartnerUserID, String aAPIKey)` opens the application and passes in the information provided in the parameters, giving the user the ability to use the provided discount code after they login or register with Parche. 