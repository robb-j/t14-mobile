package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to model an account which will belong to a user
 */
public class Account extends ModelObject {

    private String name;
    private double balance;
    private int overdraftLimit;
    private List<Transaction> transactions;
    private List<Product> products;


    public Account(int id, String name, int overdraftLimit, List<Product> products) {
        super(id);
        this.name = name;
        this.overdraftLimit = overdraftLimit;
        this.products = products;
        balance = 0;
        transactions = new ArrayList<Transaction>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(int overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
