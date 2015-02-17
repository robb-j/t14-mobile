package uk.ac.ncl.csc2022.t14.bankingapp.server;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;

/**
 * Created by Sam on 17/02/2015.
 */
public interface TransferDelegate {

    void transferPassed(Account accFrom, Account accTo);

    void transferFailed(String errMessage);
}
