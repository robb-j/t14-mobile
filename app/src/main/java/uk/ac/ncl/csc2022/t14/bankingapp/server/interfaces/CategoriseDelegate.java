package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

/**
 * Created by Jack on 12/03/2015.
 */
public interface CategoriseDelegate {

    void categorisationPassed(boolean hasNewSpin);

    void categorisationFailed(String errMessage);
}
