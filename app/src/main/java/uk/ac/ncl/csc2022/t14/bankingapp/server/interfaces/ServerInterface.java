package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;

/**
 *
 * Created by Sam on 17/02/2015.
 */
public interface ServerInterface {



    /*
     *      V1 - Basic Banking
     */

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
     * Logs the user out
     */
    void logout(LogoutDelegate delegate);




    /*
     *      V2 - Budgeting & Rewards
     */

    /**
     * @param delegate The delegate to notify with the new payments
     */
    void loadNewPaymentsForUser(NewPaymentsDelegate delegate);

    /**
     * @param categorizations The categorisations to make
     * @param delegate The delegate to notify of success
     */
    void categorisePayments(List<Categorisation> categorizations, CategoriseDelegate delegate);

    /**
     * @param updatedGroups A Map of groups that have been updated (id to group)
     * @param newGroups A List of groups that were added
     * @param deletedGroups A List of groups that were removed
     * @param delegate The delegate to notify of success
     */
    void updateBudget(Map<Integer, BudgetGroup> updatedGroups, List<BudgetGroup> newGroups, List<BudgetGroup> deletedGroups, BudgetUpdateDelegate delegate);

    /**
     * @param reward The reward chosen
     * @param delegate The delegate to notify of success
     */
    void chooseReward(Reward reward, ChooseRewardDelegate delegate);

    /**
     * @param delegate The delegate to notify of success
     */
    void performSpin(PointSpinDelegate delegate);





    /*
     *      V3 - Location Services
     */
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
}
