package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

/**
 * Class to represent essential information about a user
 */
public class User extends ModelObject {

    private String firstName;
    private String lastName;
    private Account[] accounts;
    private String dob;
    private String lastFullCategorise;

    public User(int id, String firstName, String lastName, String dob) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        accounts = new Account[10];
        lastFullCategorise = "01/01/1970";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLastFullCategorise() {
        return lastFullCategorise;
    }

    public void setLastFullCategorise(String lastFullCategorise) {
        this.lastFullCategorise = lastFullCategorise;
    }
}