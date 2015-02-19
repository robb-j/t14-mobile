package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to model an Account which belongs to a user
 */
public class Account extends ModelObject {

    private String name;
    private double balance;
    private double overdraftLimit;
    private List<Transaction> transactions;
    private Product product;


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
        setOverdraftLimit(overdraftLimit);
        setProduct(product);
        setBalance(balance);
        setTransactions(new ArrayList<Transaction>());
    }



    private void setName(String name) {
        this.name = name;
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    private void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    private void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    private void setProduct(Product product) {
        this.product = product;
    }



    public String getName() {

        return name;
    }

    public double getBalance() {
        return balance;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Product getProduct() {
        return product;
    }
}
