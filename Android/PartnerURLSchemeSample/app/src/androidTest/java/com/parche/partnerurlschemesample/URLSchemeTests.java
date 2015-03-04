package com.parche.partnerurlschemesample;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class URLSchemeTests {

    private Context        mMockContext;
    private PackageManager mMockPackageManager;

    /********************
     * SETUP / TEARDOWN *
     ********************/

    @Before
    public void setUp() {
        //Set fake context up to return fake package manager.
        mMockContext = Mockito.mock(Context.class);
        mMockPackageManager = Mockito.mock(PackageManager.class);
        Mockito.when(mMockContext.getPackageManager())
                .thenReturn(mMockPackageManager);
    }

    /**************************
     * PRIVATE HELPER METHODS *
     **************************/

    private void setMockCanOpenParche(boolean aCanOpen) {
        if (aCanOpen) {

        }
    }

    private void setMockCanOpenPlayStore(boolean aCanOpen) {
        if (aCanOpen) {

        }
    }

    private void verifyMockFiredIntentWithURLString(String urlString) {

    }

    private void verifyMockNeverFiredIntentWithURLString(String urlString) {

    }

    /****************
     * ACTUAL TESTS *
     ****************/

    @Test
    public void ifApplicationCannotBeOpenedWeShouldInstallOrUpgrade() {

    }

    @Test
    public void ifApplicationCanBeOpenedWeDontNeedToInstallOrUpgrade() {

    }

    @Test
    public void callingPlayStoreOpenWouldUseTheMarketURLIfTheMarketURLSchemeExists() {

    }

    @Test
    public void callingPlayStoreOpenWouldUseTheWebURLIfTheMarketURLSchemeDoesNotExist() {

    }

    @Test
    public void openingWithoutDiscountShouldWork() {

    }

    @Test
    public void openingWithoutDiscountShouldFailIfAppNeedsInstallOrUpgrade() {

    }

    @Test
    public void openingWithDiscountShouldWork() {

    }

    @Test
    public void openingWithDiscountShouldFailIfAppNeedsInstallOrUpgrade() {

    }
}