package br.com.socketchatroomapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by brunogabriel on 8/8/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BASE_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This function will apply title, subtitle and some actions to toolbar
     * @param mToolbar
     * @param mTitle
     * @param mSubtitle
     * @param isDisplayHomeAsUpEnabled
     * @param isDisplayShowHomeEnabled
     */
    protected void setupToolBar(Toolbar mToolbar, String mTitle, String mSubtitle,
                                boolean isDisplayHomeAsUpEnabled, boolean isDisplayShowHomeEnabled) {
        if (mToolbar!=null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(mTitle==null? "": mTitle);
            getSupportActionBar().setSubtitle(mSubtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled);
            getSupportActionBar().setDisplayShowHomeEnabled(isDisplayShowHomeEnabled);
        }
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInformation = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInformation != null && mNetworkInformation.isConnected();
    }

    /**
     * Receive an action and run it in ui thread
     * @param action
     */
    protected void runOnUI(Runnable action) {
        if (action!=null) {
            runOnUiThread(action);
        }
    }

    public void showSimpleDialog(String mTitle, String mContent) {
        if (mContent != null) {
            AlertDialog.Builder mBuilderDialog = new AlertDialog.Builder(this);

            if (mTitle != null) {
                mBuilderDialog.setTitle(mTitle);
            }

            mBuilderDialog.setMessage(mTitle);
            mBuilderDialog.setCancelable(false);
            mBuilderDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            mBuilderDialog.show();
        }
    }

    /**
     * If activity use this base, need to implement this method to organize ui code elements
     */
    public abstract void initUI();
}
