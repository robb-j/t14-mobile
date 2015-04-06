package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

/**
 * Created by Robert Hamilton on 05/04/2015.
 */
public interface CategorizeLocationDelegate {
    //groupNumber is the transaction which is getting a location
    void openMap(int groupNumber);
}
