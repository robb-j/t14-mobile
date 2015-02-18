package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

/**
 * Class used to represent a transaction, which will be made between 2 accounts
 */
public class Transaction extends ModelObject {

    private double amount;
    private Account account;
    private String payee;

    public Transaction(int id, double amount, Account account, String payee) {
        super(id);
        this.amount = amount;
        this.account = account;
        this.payee = payee;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
