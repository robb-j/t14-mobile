package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Class used to represent a transaction, which will be made between 2 accounts
 */
public class Transaction extends ModelObject implements Parcelable {

    private double amount;
    private Account account;
    private String payee;
    private Date date;
    private boolean categorised;
    private String category;


    /**
     * Creates a new Transaction model
     * @param id The unique identifier for this Transactoin
     * @param amount How much money was transferred
     * @param account The account the transation was applied to
     * @param payee Where the money was paid to
     */
    public Transaction(int id, double amount, Date date,  Account account, String payee) {
        super(id);
        setAmount(amount);
        setAccount(account);
        setPayee(payee);
        setDate(date);
    }

    public double getAmount() {
        return amount;
    }
    private void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }
    private void setAccount(Account account) {
        this.account = account;
    }

    public String getPayee() {
        return payee;
    }
    private void setPayee(String payee) {
        this.payee = payee;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getCategorised(){
        return categorised;
    }
    public void setCategorised(boolean categorized) {
        this.categorised = categorized;
    }

    public String getCategory(){
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    protected Transaction(Parcel in) {
        amount = in.readDouble();
        account = (Account) in.readValue(Account.class.getClassLoader());
        payee = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeValue(account);
        dest.writeString(payee);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}