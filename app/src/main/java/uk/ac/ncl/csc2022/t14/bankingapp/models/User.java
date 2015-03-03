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
public class User extends ModelObject implements Parcelable {

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

    protected User(Parcel in) {
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0x01) {
            accounts = new ArrayList<Account>();
            in.readList(accounts, Account.class.getClassLoader());
        } else {
            accounts = null;
        }
        dob = in.readString();
        lastFullCategorise = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        if (accounts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(accounts);
        }
        dest.writeString(dob);
        dest.writeString(lastFullCategorise);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}