package uk.ac.ncl.csc2022.t14.bankingapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.BankingApp;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.LoginActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;

/**
 * Created by Sam on 13/04/2015.
 */
public class LloydsActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(this).inflate(R.layout.fragment_action_bar, null);
        getSupportActionBar().setCustomView(customNav, lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        BankingApp.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* Error checking - if the user does not exist anymore, restart the app */
        if (!(this instanceof LoginActivity) && DataStore.sharedInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            startActivity(mainIntent);
        }
    }

    @Override
    protected void onDestroy() {
        ((LiveServerConnector)DataStore.sharedInstance().getConnector()).removeLoadingSpinner();
        super.onDestroy();
    }
}
