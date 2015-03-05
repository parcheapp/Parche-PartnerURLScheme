package com.parche.partnerurlschemesample;

import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Helper class to assist in calling Parche's URL scheme.
 */
public class ParchePartnerURLSchemeHelper {

    private static final String URL_SCHEME = "goparche://";
    private static final String OPEN_ENDPOINT = "open";
    private static final String NO_DISCOUNT_FORMAT = "?api_key=%s";
    private static final String DISCOUNT_FORMAT = "?partner_user_id=%s&discount_code=%s&api_key=%s";

    public static final String PARCHE_PACKAGE_NAME = "com.parche.parchemobile";
    public static final String PLAY_STORE_URL_SCHEME = "market://details?id=";
    public static final String PLAY_STORE_WEB_URL = "http://play.google.com/store/apps/details?id=";


    private Intent mIntent;

    /*******************
     * PRIVATE METHODS *
     *******************/

     private static ParchePartnerURLSchemeHelper sSharedInstance = null;
     private static ParchePartnerURLSchemeHelper sharedInstance() {
         if (sSharedInstance == null) {
             sSharedInstance = new ParchePartnerURLSchemeHelper();
         }

         return sSharedInstance;
     }

    /*******************
     * TESTING METHODS *
     *******************/

    /*
       The following methods are protected so they may be accessed by tests.
     */

    protected static void setIntent(Intent aIntent) {
        sharedInstance().mIntent = aIntent;
    }

    protected static Intent getViewIntentForURLString(String aURLString) {
        Intent intentToUse = sharedInstance().mIntent;
        if (intentToUse == null) {
            intentToUse = new Intent();
        }

        intentToUse.setAction(Intent.ACTION_VIEW);
        intentToUse.setData(Uri.parse(aURLString));

        return intentToUse;
    }

    /******************
     * PUBLIC METHODS *
     ******************/

    /**
     * Determines if there is a version of Parche on the user's device which responds
     * to this URL scheme.
     *
     * @param aContext The current context.
     *
     * @return true if the application needs to be updated or installed, false if you're clear
     *         to go ahead and open the application.
     */
    public static boolean parcheNeedsToBeUpdatedOrInstalled(Context aContext) {
        PackageManager packageManager = aContext.getPackageManager();
        Intent openAppIntent = getViewIntentForURLString(URL_SCHEME + OPEN_ENDPOINT);
        List activitiesCanHandle = packageManager.queryIntentActivities(openAppIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return activitiesCanHandle.size() == 0;
    }

    /**
     * Opens the Google Play store to show the Parche application so it may be updated or installed.
     * Will try to open the Google Play store first, then fall back to opening the app's page on the Play store website.
     *
     * @param aContext The current context.
     */
    //Intent.FLAG_ACTIVITY_CLEAR_TASK_WHEN_RESET deprecated in API 21, but still needed before that.
    @SuppressWarnings("deprecation")
    public static void showParcheInPlayStore(Context aContext) {
        String marketURLString = PLAY_STORE_URL_SCHEME + PARCHE_PACKAGE_NAME;
        Intent marketIntent = getViewIntentForURLString(marketURLString);
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            aContext.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            //This user does not have the Play app installed - show them the webpage.
            String webURLString = PLAY_STORE_WEB_URL + PARCHE_PACKAGE_NAME;
            Intent webIntent = getViewIntentForURLString(webURLString);
            aContext.startActivity(webIntent);
        }
    }

    /**
     * Opens the Parche application without a discount, but indicating what app the request
     * is coming from.
     *
     * @param aContext  The current context.
     * @param aAPIKey   The partner application's Parche API key.
     *
     * @return true if the request opened the Parche application successfully, false if it did not.
     */
    public static boolean openParche(Context aContext, String aAPIKey) {
        if (!parcheNeedsToBeUpdatedOrInstalled(aContext)) {
            String urlString = URL_SCHEME + OPEN_ENDPOINT + String.format(NO_DISCOUNT_FORMAT, aAPIKey);
            Intent openOnlyIntent = getViewIntentForURLString(urlString);
            aContext.startActivity(openOnlyIntent);
            return true;
        }

        return false;
    }

    /**
     * Opens the Parche application and passes the required information to provide a user discount
     * along to the app.
     *
     * @param aContext          The current context.
     * @param aDiscountCode     The discount code retrieved from the Parche server.
     * @param aPartnerUserID    The user ID to identify the user. NOTE: Will be url-encoded this
     *                          class, DO NOT URL ENCODE before passing in.
     * @param aAPIKey           The partner applications Parche API key.
     *
     * @return true if the request opened the Parche application successfully, false if it did not.
     */
    public static boolean openParcheAndRequestDiscount(Context aContext,
                                                       String aDiscountCode,
                                                       String aPartnerUserID,
                                                       String aAPIKey) {
        if (!parcheNeedsToBeUpdatedOrInstalled(aContext)) {
            String urlString = URL_SCHEME + OPEN_ENDPOINT + String.format(DISCOUNT_FORMAT, aPartnerUserID, aDiscountCode, aAPIKey);
            Intent discountIntent = getViewIntentForURLString(urlString);
            aContext.startActivity(discountIntent);
            return true;
        }

        return false;
    }
}
