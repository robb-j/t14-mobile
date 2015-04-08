package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class used to model an Account which belongs to a user
 */
public class Account extends ModelObject implements Parcelable {

    private String name;
    private double balance;
    private double overdraftLimit;
    private List<Transaction> transactions;
    private Product product;
    private Date firstTransaction;


    /**
     * Creates a new Account instance
     * @param id The unique identifier for this Account
     * @param name A friendly name for this Account
     * @param balance How much money is in this Account
     * @param overdraftLimit The max amount of debt the account can go to
     * @param product The product that represents this Account
     */
    public Account(int id, String name, double balance, double overdraftLimit, Product product) {
        super(id);
        setName(name);
        setBalance(balance);
        setOverdraftLimit(overdraftLimit);
        setProduct(product);
        setTransactions(new ArrayList<Transaction>());
    }

    public Account(int id, String name, double balance, double overdraftLimit) {
        super(id);
        setName(name);
        setBalance(balance);
        setOverdraftLimit(overdraftLimit);
        setTransactions(new ArrayList<Transaction>());
    }

    public String getName() {
        return name;
    }
    private void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    private void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
    private void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Date getFirstTransaction() {
        return firstTransaction;
    }
    public void setFirstTransaction(Date date) {
        firstTransaction = date;
    }

    protected Account(Parcel in) {
        name = in.readString();
        balance = in.readDouble();
        overdraftLimit = in.readDouble();
        if (in.readByte() == 0x01) {
            transactions = new ArrayList<Transaction>();
            in.readList(transactions, Transaction.class.getClassLoader());
        } else {
            transactions = null;
        }
        product = (Product) in.readValue(Product.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(balance);
        dest.writeDouble(overdraftLimit);
        if (transactions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(transactions);
        }
        dest.writeValue(product);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}