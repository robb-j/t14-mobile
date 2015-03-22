package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 06/03/2015.
 */
public class BudgetCategory extends ModelObject {

    private String name;
    private double budgeted;
    private double spent;

    public BudgetCategory(int id, int budgeted) {
        super(id);
        setName(name);
        setBudgeted(budgeted);
        setSpent(0);
    }

    public String getName() {
        return name;
    }
    private void setName(String name) {
        this.name = name;
    }

    public double getBudgeted() {
        return budgeted;
    }
    private void setBudgeted(int budgeted) {
        this.budgeted = budgeted;
    }

    public double getSpent() {
        return spent;
    }
    private void setSpent(double spent) {
        this.spent = spent;
    }
}
