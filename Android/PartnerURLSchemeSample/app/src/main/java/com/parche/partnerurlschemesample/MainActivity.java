package com.parche.partnerurlschemesample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import butterknife.*;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.can_open_textview) TextView mCanOpenTextView;

    /**********************
     * ACTIVITY LIFECYCLE *
     **********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    /**********
     * ALERTS *
     **********/

    private void showAlert(@StringRes int aTitleResourceID, @StringRes int aMessageResourceID) {
        new AlertDialog.Builder(this)
                .setTitle(aTitleResourceID)
                .setMessage(aMessageResourceID)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /*********************
     * ONCLICK LISTENERS *
     *********************/

    @OnClick(R.id.check_install_button)
    public void checkParcheNeedsUpdateOrInstall() {
        boolean needs = ParchePartnerURLSchemeHelper.parcheNeedsToBeUpdatedOrInstalled(this);
        String needsText = needs ? "YES" : "NO";
        mCanOpenTextView.setText(needsText);
    }

    @OnClick(R.id.show_in_store_button)
    public void showParcheInPlayStore() {
        Intent openPlayStoreIntent = ParchePartnerURLSchemeHelper.showParcheInPlayStoreIntent(this);
        startActivity(openPlayStoreIntent);
    }

    @OnClick(R.id.open_no_discount_button)
    public void openWithoutDiscount() {
        Intent openIntent = ParchePartnerURLSchemeHelper.openParcheIntent(this, "FAKE_API_KEY");
        if (openIntent == null) {
            showAlert(R.string.not_installed_alert_title, R.string.not_installed_alert_message);
        } else {
            startActivity(openIntent);
        }
    }
}
