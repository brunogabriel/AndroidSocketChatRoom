package br.com.socketchatroomapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import br.com.socketchatroomapp.manager.ChatManager;
import butterknife.ButterKnife;

/**
 * Created by brunogabriel on 8/8/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BASE_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Applying StrictMode and ButterKnife debug
        if (ChatManager.DEVELOPER_MODE) {
            Log.d(TAG, "Starting strict mode in development");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            ButterKnife.setDebug(true);
        }
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

    /**
     * If activity use this base, need to implement this method to organize ui code elements
     */
    public abstract void initUI();
}
