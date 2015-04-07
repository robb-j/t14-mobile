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
     */
    void loadTransactions(Account account, int month, int year, TransactionDelegate delegate);

    /**
     * @param accountA Account transferring from.
     * @param accountB Account transferring to.
     * @param amount Amount to be transferred.
     */
    void makeTransfer(Account accountA, Account accountB, double amount, TransferDelegate delegate);

    /**
     * @param delegate The delegate to notify of success or failure
     */
    void loadATMS(ATMDelegate delegate);

    /**
     * @param accounts The ids of the account to filter by, if empty will use them all
     * @param start When to start filtering by, if null will default to the start of time
     * @param end When to stop the filter, if null will default to today
     * @param delegate The delegate to notify of success or failure
     */

    void loadHeatMap(int[] accounts, Date start, Date end, HeatMapDelegate delegate);
	
	/**
	 * Logs the user out
	 */
    void logout();
}
