package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.ac.ncl.csc2022.t14.bankingapp.activities.LoginActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LogoutDelegate;

/**
 * Created by Sam on 08/04/2015.
 */
public class LogoutButton extends Button implements LogoutDelegate {


    public LogoutButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                DataStore.sharedInstance().getConnector().logout(LogoutButton.this);
            }
        });
    }

    public LogoutButton(Context context) {

        super(context);
    }

    @Override
    public void logoutPassed() {
        if (!(getContext() instanceof LoginActivity)) {
            Toast.makeText(getContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show();

            returnToLogin();
        }

    }

    @Override
    public void logoutFailed(String message) {

        if (!(getContext() instanceof LoginActivity)) {
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();

            returnToLogin();
        }
    }


    public void returnToLogin() {

        Intent intent = new Intent(getContext(), LoginActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        getContext().startActivity(mainIntent);
    }
}
