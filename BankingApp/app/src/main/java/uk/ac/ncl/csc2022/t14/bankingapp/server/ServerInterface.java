package uk.ac.ncl.csc2022.t14.bankingapp.server;

import java.util.Date;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;

/**
 * Created by Sam on 17/02/2015.
 */
public interface ServerInterface {

    /**
     * @param username Username entered by user.
     * @param password The characters the user has entered.
     * @param indices The indices of the characters used in the password.
     */
    void login(String username, List<Character> password, List<Integer> indices, LoginDelegate delegate);

    /**
     * @param account The account to load transactions from.
     * @param month Month of transactions to load.
     * @param token Token required to authenticate session.
     */
    void loadTransactions(Account account, Date month, String token, TransactionsDelegate delegate);

    /**
     * @param accFrom Account transferring from.
     * @param accTo Account transferring to.
     * @param amount Amount to be transferred.
     * @param token Token required to authenticate session.
     */
    void makeTransfer(Account accFrom, Account accTo, double amount, String token, TransferDelegate delegate);

}
