package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import android.view.View;

/**
 * Created by Robert Hamilton on 05/04/2015.
 */
public interface CategorizeLocationDelegate {
    //groupNumber is the transaction which is getting a location
    void openMap(int groupNumber);

    //GroupNumber and view refer to the transaction which has been given a location
    void locationConfirmed(int groupNumber, View view);
}
