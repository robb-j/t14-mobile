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


    /**
     * Creates a new Transaction model
     * @param id The unique identifier for this Transactoin
     * @param amount How much money was transferred
     * @param account The account the transation was applied to
     * @param payee Where the money was paid to
     */
    public Transaction(int id, double amount, Account account, String payee) {
        super(id);
        setAmount(amount);
        setAccount(account);
        setPayee(payee);
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
}
