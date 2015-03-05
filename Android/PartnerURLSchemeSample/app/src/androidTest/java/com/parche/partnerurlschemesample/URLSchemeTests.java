package com.parche.partnerurlschemesample;

import android.content.*;
import android.content.pm.*;
import android.support.test.runner.AndroidJUnit4;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ParchePartnerURLSchemeHelper.setIntent(new Intent());
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

        Intent viewIntent = ParchePartnerURLSchemeHelper.getViewIntentForURLString("goparche://open");
        when(mMockPackageManager.queryIntentActivities(viewIntent, PackageManager.MATCH_DEFAULT_ONLY))
                .thenReturn(activitiesList);
    }

    private void setMockCanOpenPlayStore(boolean aCanOpen) {
        if (aCanOpen) {

        }
    }

    private void verifyMockFiredIntentWithURLString(String urlString) {
        Intent viewIntent = ParchePartnerURLSchemeHelper.getViewIntentForURLString(urlString);
        verify(mMockContext, times(1)).startActivity(viewIntent);
    }

    private void verifyMockNeverFiredIntentWithURLString(String urlString) {
        Intent viewIntent = ParchePartnerURLSchemeHelper.getViewIntentForURLString(urlString);
        verify(mMockContext, never()).startActivity(viewIntent);
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
        Intent viewIntent = ParchePartnerURLSchemeHelper.getViewIntentForURLString("goparche://open");

        verify(mMockPackageManager, times(1)).queryIntentActivities(eq(viewIntent), eq(PackageManager.MATCH_DEFAULT_ONLY));
        assertFalse(shouldUpgrade);
    }

    @Test
    public void callingPlayStoreOpenWouldUseTheMarketURLIfTheMarketURLSchemeExists() {

    }

    @Test
    public void callingPlayStoreOpenWouldUseTheWebURLIfTheMarketURLSchemeDoesNotExist() {

    }

    @Test
    public void openingWithoutDiscountShouldWork() {
        setMockCanOpenParche(true);
        boolean canOpen = ParchePartnerURLSchemeHelper.openParche(mMockContext, TEST_API_KEY);
        assertTrue(canOpen);
        verifyMockFiredIntentWithURLString(EXPECTED_NO_DISCOUNT_URL_STRING);
    }

    @Test
    public void openingWithoutDiscountShouldFailIfAppNeedsInstallOrUpgrade() {
        setMockCanOpenParche(false);
        boolean canOpen = ParchePartnerURLSchemeHelper.openParche(mMockContext, TEST_API_KEY);
        assertFalse(canOpen);
        verifyMockNeverFiredIntentWithURLString(EXPECTED_NO_DISCOUNT_URL_STRING);
    }

    @Test
    public void openingWithDiscountShouldWork() {
        setMockCanOpenParche(true);
        boolean canOpen = ParchePartnerURLSchemeHelper.openParcheAndRequestDiscount(mMockContext,
                TEST_DISCOUNT_CODE,
                TEST_STANDARD_USER_ID,
                TEST_API_KEY);
        assertTrue(canOpen);
        verifyMockFiredIntentWithURLString(EXPECTED_STANDARD_DISCOUNT_URL_STRING);
    }

    @Test
    public void openingWithDiscountShouldFailIfAppNeedsInstallOrUpgrade() {
        setMockCanOpenParche(false);
        boolean canOpen = ParchePartnerURLSchemeHelper.openParcheAndRequestDiscount(mMockContext,
                TEST_DISCOUNT_CODE,
                TEST_STANDARD_USER_ID,
                TEST_API_KEY);
        assertFalse(canOpen);
        verifyMockNeverFiredIntentWithURLString(EXPECTED_STANDARD_DISCOUNT_URL_STRING);
    }
}