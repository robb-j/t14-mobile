package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Robert Hamilton on 06/04/2015.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks
{
    private int resumed;
    private int stopped;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is being backgrounded: " + (resumed == stopped));
        if(resumed==stopped) {
            System.exit(0);
        }
    }
}
