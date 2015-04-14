package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import android.app.Application;
import android.content.Context;

/**
 * Created by Robert Hamilton on 06/04/2015.
 */
public class BankingApp extends Application
{

    private static Context mContext;


    @Override
    public void onCreate()
    {
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
