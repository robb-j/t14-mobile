package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

/**
 * An object that logs the User out
 * Created by Rob A on 07/04/15.
 */
public interface LogoutDelegate {

    void logoutPassed();

    void logoutFailed(String message);
}
