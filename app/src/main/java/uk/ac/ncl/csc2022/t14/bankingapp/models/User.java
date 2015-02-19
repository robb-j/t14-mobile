package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent essential information about a user
 */
public class User extends ModelObject {

    private String username;
    private String firstName;
    private String lastName;
    private List<Account> accounts;
    private String dob;
    private String lastFullCategorise;


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
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setDob(dob);
        setAccounts(new ArrayList<Account>());
        setLastFullCategorise("01/01/1970");
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
}
