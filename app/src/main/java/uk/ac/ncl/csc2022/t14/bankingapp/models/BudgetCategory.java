package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 06/03/2015.
 */

/**
 * Class used to model a BudgetCategory. Not to be confused with a BudgetGroup, which contains a list of BudgetCategories.
 */
public class BudgetCategory extends ModelObject {

    public static final int TYPE_NEW = -1;
    public static final int TYPE_REMOVED = -2;

    private String name;
    private double budgeted;
    private double spent;

    /**
     * Creates a new BudgetCategory instance
     * @param id The unique identifier for this BudgetCategory
     * @param name A friendly name for this BudgetCategory
     * @param budgeted The amount of money budgeted for this category
     */
    public BudgetCategory(int id, String name, double budgeted) {
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
    private void setBudgeted(double budgeted) {
        this.budgeted = budgeted;
    }

    public double getSpent() {
        return spent;
    }
    private void setSpent(double spent) {
        this.spent = spent;
    }

    public void setDeleted() {
        setId(TYPE_REMOVED);
    }
}
