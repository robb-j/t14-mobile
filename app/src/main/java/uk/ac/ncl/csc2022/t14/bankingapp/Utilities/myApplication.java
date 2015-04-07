package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import android.app.Application;

/**
 * Created by Robert Hamilton on 06/04/2015.
 */
public class myApplication extends Application
{
    @Override
    public void onCreate()
    {
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }
}
