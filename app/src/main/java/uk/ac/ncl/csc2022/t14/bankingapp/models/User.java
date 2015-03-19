package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent essential information about a user
 */
public class User extends ModelObject {

    // Banking fields
    private String username;
    private String firstName;
    private String lastName;
    private List<Account> accounts;
    private String dob;
    private String lastFullCategorise;

    // Budgeting fields
    private MonthBudget currentBudget;
    private List<BudgetGroup> allGroups;
    private List<Reward> recentRewards;
    private List<PointGain> recentPoints;
    private int numberOfSpins;
    private int numNewPayments;

    /**
     * Creates a new User model
     * @param id The unique identifier for this user
     * @param username The username of the user
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param dob The date of birth of the user
     */
    public User(int id, String username, String firstName, String lastName, String dob) {

        super(id);

        // Set banking fields
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setDob(dob);
        setAccounts(new ArrayList<Account>());
        setLastFullCategorise("01/01/1970");

        // Set budgeting fields
        allGroups = new ArrayList<>();
        recentRewards = new ArrayList<>();
        recentPoints = new ArrayList<>();
        setNumberOfSpins(0);
        setNumNewPayments(0);
    }


    /**
     * Gets an account for a given id
     * @param accountId The Id of the account you want
     */
    public Account getAccountForId(int accountId) {

        for (Account account : accounts) {

            if (account.getId() == accountId) {

                return account;
            }
        }
        return null;
    }


    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    private void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getDob() {
        return dob;
    }

    private void setDob(String dob) {
        this.dob = dob;
    }

    public String getLastFullCategorise() {
        return lastFullCategorise;
    }

    public void setLastFullCategorise(String lastFullCategorise) {
        this.lastFullCategorise = lastFullCategorise;
    }

    public MonthBudget getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(MonthBudget currentBudget) {
        this.currentBudget = currentBudget;
    }

    public List<BudgetGroup> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<BudgetGroup> allGroups) {
        this.allGroups = allGroups;
    }

    public List<Reward> getRecentRewards() {
        return recentRewards;
    }

    public void setRecentRewards(List<Reward> recentRewards) {
        this.recentRewards = recentRewards;
    }

    public List<PointGain> getRecentPoints() {
        return recentPoints;
    }

    public void setRecentPoints(List<PointGain> recentPoints) {
        this.recentPoints = recentPoints;
    }

    public int getNumberOfSpins() {
        return numberOfSpins;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public int getNumNewPayments() {
        return numNewPayments;
    }

    public void setNumNewPayments(int numNewPayments) {
        this.numNewPayments = numNewPayments;
    }
}