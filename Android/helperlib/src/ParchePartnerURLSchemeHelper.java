package com.parche.partnerurlschemesample.helperlib;

import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;

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

    /*******************
     * PRIVATE METHODS *
     *******************/

    private static boolean urlCanBeHandled(Context aContext, String aURLString) {
        PackageManager packageManager = aContext.getPackageManager();
        Intent openAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aURLString));
        List activitiesCanHandle = packageManager.queryIntentActivities(openAppIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return activitiesCanHandle.size() != 0;
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
        return !urlCanBeHandled(aContext, URL_SCHEME + OPEN_ENDPOINT);
    }

    /**
     * Creates an intent to open either the Play Store or the web page for the play store which will show Parche,
     * depending on whether the user has the Play Store installed or not.
     *
     * @param aContext The current context.
     * @return An Intent which can be used to launch the appropriate route to the Play Store.
     */
    //Intent.FLAG_ACTIVITY_CLEAR_TASK_WHEN_RESET deprecated in API 21, but still needed before that.
    @SuppressWarnings("deprecation")
    public static Intent showParcheInPlayStoreIntent(Context aContext) {
        String marketURLString = PLAY_STORE_URL_SCHEME + PARCHE_PACKAGE_NAME;
        Intent returnIntent;
        if (urlCanBeHandled(aContext, marketURLString)) {
            returnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketURLString));
        } else {
            //This user does not have the Play app installed - show them the webpage.
            String webURLString = PLAY_STORE_WEB_URL + PARCHE_PACKAGE_NAME;
            returnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURLString));
        }

        return returnIntent;
    }

    /**
     * Creates an Intent to open the Parche application without a discount, but indicating what app the request
     * is coming from.
     *
     * @param aContext  The current context.
     * @param aAPIKey   The partner application's Parche API key.
     *
     * @return The intent to open the Parche application, or null if the app needs to be updated or installed.
     */
    public static Intent openParcheIntent(Context aContext, String aAPIKey) {
        Intent returnIntent = null;
        if (!parcheNeedsToBeUpdatedOrInstalled(aContext)) {
            String urlString = URL_SCHEME + OPEN_ENDPOINT + String.format(NO_DISCOUNT_FORMAT, aAPIKey);
            returnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        }

        return returnIntent;
    }

    /**
     * Creates an Intent to open Parche application and pass the required information to provide a user discount
     * along to the app.
     *
     * @param aContext          The current context.
     * @param aDiscountCode     The discount code retrieved from the Parche server.
     * @param aPartnerUserID    The user ID to identify the user. NOTE: Will be url-encoded this
     *                          class, DO NOT URL ENCODE before passing in.
     * @param aAPIKey           The partner applications Parche API key.
     *
     * @return The intent to open the Parche application, or null if the app needs to be updated or installed.
     */
    public static Intent openParcheAndRequestDiscount(Context aContext,
                                                      String aDiscountCode,
                                                      String aPartnerUserID,
                                                      String aAPIKey) {
        Intent returnIntent = null;
        if (!parcheNeedsToBeUpdatedOrInstalled(aContext)) {
            String urlString = URL_SCHEME + OPEN_ENDPOINT + String.format(DISCOUNT_FORMAT, aPartnerUserID, aDiscountCode, aAPIKey);
            returnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        }

        return returnIntent;
    }
}
