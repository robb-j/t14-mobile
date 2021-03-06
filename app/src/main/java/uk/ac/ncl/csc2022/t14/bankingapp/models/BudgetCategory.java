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

    public enum Mode { UNCHANGED, REMOVED, EDITED, NEW }

    Mode mode = Mode.UNCHANGED;

    private String name;
    private double budgeted;
    private double spent;

    /**
     * Creates a new BudgetCategory instance
     * @param id The unique identifier for this BudgetCategory
     * @param name A friendly name for this BudgetCategory
     * @param budgeted The amount of money budgeted for this category
     */
    public BudgetCategory(int id, String name, double budgeted, double spent) {
        super(id);
        setName(name);
        setBudgeted(budgeted);
        setSpent(spent);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getBudgeted() {
        return budgeted;
    }
    public void setBudgeted(double budgeted) {
        this.budgeted = budgeted;
    }

    public double getSpent() {
        return spent;
    }
    public void setSpent(double spent) {
        this.spent = spent;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode != Mode.NEW) {
            this.mode = mode;
        }

    }

    public void setDeleted() {
        setId(TYPE_REMOVED);
    }
}
