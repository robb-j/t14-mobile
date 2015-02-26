package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.Date;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;

/**
 * Created by Sam on 17/02/2015.
 */
public interface ServerInterface {



    /**
     * @param password The characters the user has entered.
     * @param indices The indices of the characters used in the password.
     */
    void login(String username, char[] password, int[] indices, LoginDelegate delegate);

    /**
     * @param month Month of transactions to load.
     * @param token Token required to authenticate session.
     */
    void loadTransactions(Account account, int month, String token, TransactionDelegate delegate);

    /**
     * @param accFrom Account transferring from.
     * @param accTo Account transferring to.
     * @param amount Amount to be transferred.
     * @param token Token required to authenticate session.
     */
    void makeTransfer(Account accFrom, Account accTo, double amount, String token, TransferDelegate delegate);

}
