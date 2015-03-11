package com.parche.partnerurlschemesample;

import android.content.*;
import android.content.pm.*;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import com.parche.partnerurlschemesample.helperlib.ParchePartnerURLSchemeHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * NOTE: Due to a known issue with Mockito and ART, these tests will not run on Lollipop+ devices.
 *
 * https://code.google.com/p/android-developer-preview/issues/detail?id=1081
 * http://stackoverflow.com/a/20514589/681493
 */

@RunWith(AndroidJUnit4.class)
public class URLSchemeTests {

    private static final String TEST_API_KEY = "FAKE_API_KEY";
    private static final String TEST_DISCOUNT_CODE = "DISCOUNT_CODE";
    private static final String TEST_STANDARD_USER_ID = "USER_ID";
    private static final String EXPECTED_NO_DISCOUNT_URL_STRING = "goparche://open?api_key=FAKE_API_KEY";
    private static final String EXPECTED_STANDARD_DISCOUNT_URL_STRING = "goparche://open?partner_user_id=USER_ID&discount_code=DISCOUNT_CODE&api_key=FAKE_API_KEY";

    private Context        mMockContext;
    private PackageManager mMockPackageManager;

    /********************
     * SETUP / TEARDOWN *
     ********************/

    @Before
    public void setUp() {
        //Set fake context up to return fake package manager.
        mMockContext = mock(Context.class);
        mMockPackageManager = mock(PackageManager.class);
        when(mMockContext.getPackageManager())
                .thenReturn(mMockPackageManager);
    }

    /**************************
     * PRIVATE HELPER METHODS *
     **************************/

    private void setMockCanOpenParche(boolean aCanOpen) {
        List<ResolveInfo> activitiesList = new ArrayList<>();
        if (aCanOpen) {
            ResolveInfo appInfo = new ResolveInfo();
            appInfo.resolvePackageName = ParchePartnerURLSchemeHelper.PARCHE_PACKAGE_NAME;
            appInfo.activityInfo = new ActivityInfo();
            activitiesList.add(appInfo);
        }

        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("goparche://open"));
        //Use "reflection equals" since intents don't work with equals.
        when(mMockPackageManager.queryIntentActivities(refEq(viewIntent), eq(PackageManager.MATCH_DEFAULT_ONLY)))
                .thenReturn(activitiesList);
    }

    private void setMockCanOpenPlayStore(boolean aCanOpen) {
        List<ResolveInfo> activitiesList = new ArrayList<>();
        if (aCanOpen) {
            ResolveInfo appInfo = new ResolveInfo();
            appInfo.resolvePackageName = "ANDROIDMARKET";
            appInfo.activityInfo = new ActivityInfo();
            activitiesList.add(appInfo);
        }

        String urlString = ParchePartnerURLSchemeHelper.PLAY_STORE_URL_SCHEME + ParchePartnerURLSchemeHelper.PARCHE_PACKAGE_NAME;
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

        //Use "reflection equals" since intents don't work with equals.
        when(mMockPackageManager.queryIntentActivities(refEq(marketIntent), eq(PackageManager.MATCH_DEFAULT_ONLY)))
                .thenReturn(activitiesList);
    }

    /****************
     * ACTUAL TESTS *
     ****************/

    @Test
    public void ifApplicationCannotBeOpenedWeShouldInstallOrUpgrade() {
        setMockCanOpenParche(false);
        boolean shouldUpgrade = ParchePartnerURLSchemeHelper.parcheNeedsToBeUpdatedOrInstalled(mMockContext);
        assertTrue(shouldUpgrade);
    }

    @Test
    public void ifApplicationCanBeOpenedWeDontNeedToInstallOrUpgrade() {
        setMockCanOpenParche(true);
        boolean shouldUpgrade = ParchePartnerURLSchemeHelper.parcheNeedsToBeUpdatedOrInstalled(mMockContext);
        assertFalse(shouldUpgrade);
    }

    @Test
    public void callingPlayStoreOpenWouldUseTheMarketURLIfTheMarketURLSchemeExists() {
        setMockCanOpenPlayStore(true);
        String urlString = ParchePartnerURLSchemeHelper.PLAY_STORE_URL_SCHEME + ParchePartnerURLSchemeHelper.PARCHE_PACKAGE_NAME;
        Intent openPlayStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        Intent fromHelper = ParchePartnerURLSchemeHelper.showParcheInPlayStoreIntent(mMockContext);
        assertTrue(fromHelper.filterEquals(openPlayStoreIntent));
    }

    @Test
    public void callingPlayStoreOpenWouldUseTheWebURLIfTheMarketURLSchemeDoesNotExist() {
        setMockCanOpenPlayStore(false);
        ParchePartnerURLSchemeHelper.showParcheInPlayStoreIntent(mMockContext);

        String urlString = ParchePartnerURLSchemeHelper.PLAY_STORE_WEB_URL + ParchePartnerURLSchemeHelper.PARCHE_PACKAGE_NAME;
        Intent openWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

        Intent fromHelper = ParchePartnerURLSchemeHelper.showParcheInPlayStoreIntent(mMockContext);
        assertTrue(fromHelper.filterEquals(openWebIntent));
    }

    @Test
    public void openingWithoutDiscountShouldWork() {
        setMockCanOpenParche(true);
        Intent openWithoutDiscount = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPECTED_NO_DISCOUNT_URL_STRING));
        Intent fromHelper = ParchePartnerURLSchemeHelper.openParcheIntent(mMockContext, TEST_API_KEY);
        assertNotNull(fromHelper);
        assertTrue(fromHelper.filterEquals(openWithoutDiscount));
    }

    @Test
    public void openingWithoutDiscountShouldFailIfAppNeedsInstallOrUpgrade() {
        setMockCanOpenParche(false);
        Intent fromHelper = ParchePartnerURLSchemeHelper.openParcheIntent(mMockContext, TEST_API_KEY);
        assertNull(fromHelper);
    }

    @Test
    public void openingWithDiscountShouldWork() {
        setMockCanOpenParche(true);
        Intent openWithDiscount = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPECTED_STANDARD_DISCOUNT_URL_STRING));
        Intent fromHelper = ParchePartnerURLSchemeHelper.openParcheAndRequestDiscount(mMockContext,
                TEST_DISCOUNT_CODE,
                TEST_STANDARD_USER_ID,
                TEST_API_KEY);
        assertNotNull(fromHelper);
        assertTrue(fromHelper.filterEquals(openWithDiscount));
    }

    @Test
    public void openingWithDiscountShouldFailIfAppNeedsInstallOrUpgrade() {
        setMockCanOpenParche(false);
        Intent fromHelper = ParchePartnerURLSchemeHelper.openParcheAndRequestDiscount(mMockContext,
                TEST_DISCOUNT_CODE,
                TEST_STANDARD_USER_ID,
                TEST_API_KEY);
        assertNull(fromHelper);
    }
}