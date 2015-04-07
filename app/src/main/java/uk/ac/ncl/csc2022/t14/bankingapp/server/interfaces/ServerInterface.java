package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

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
    void loadTransactions(Account account, int month, int year, String token, TransactionDelegate delegate);

    /**
     * @param accFrom Account transferring from.
     * @param accTo Account transferring to.
     * @param amount Amount to be transferred.
     * @param token Token required to authenticate session.
     */
    void makeTransfer(int accFrom, int accTo, double amount, String token, TransferDelegate delegate);

    /**
     * @param delegate The delegate to notify of success or failure
     */
    void loadATMS(ATMDelegate delegate);

    /**
     * @param delegate The delegate to notify of success or failure
     */
    void loadHeatMap(HeatMapDelegate delegate);

    void updateBudget(BudgetUpdateDelegate delegate, HashMap<Integer, BudgetGroup> updatedGroups, ArrayList<BudgetGroup> newGroups, ArrayList<BudgetGroup> deletedGroups);

    //void logout();
}
