package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 12/03/2015.
 */
public class Categorisation extends ModelObject {

    private Transaction transaction;
    private BudgetCategory budgetCategory;

    public Categorisation(int id) {
        super(id);
    }

    public Transaction getTransaction() {
        return transaction;
    }
    private void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public BudgetCategory getBudgetCategory() {
        return budgetCategory;
    }
    private void setBudgetCategory(BudgetCategory budgetCategory) {
        this.budgetCategory = budgetCategory;
    }
}
