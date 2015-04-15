package uk.ac.ncl.csc2022.t14.bankingapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 06/03/2015.
 */

/**
 * Class used to model a BudgetGroup. Contains a list of BudgetCategories.
 */
public class BudgetGroup extends ModelObject {

    public static final int TYPE_NEW = -1;
    public static final int TYPE_REMOVED = -2;
    private enum Mode { UNCHANGED, REMOVED, EDITED, NEW }

    Mode mode = Mode.UNCHANGED;

    private String name;
    private List<BudgetCategory> categories;

    /**
     * Creates a new BudgetGroup instance
     * @param id The unique identifier for this BudgetGroup
     * @param name A friendly name for this BudgetGroup
     */
    public BudgetGroup(int id, String name) {
        super(id);
        setName(name);
        categories = new ArrayList<>();
        setMode(Mode.UNCHANGED);
    }

    public BudgetGroup(BudgetGroup budgetGroup) {
        budgetGroup = new BudgetGroup(getId(), getName());
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }
    private void setCategories(List<BudgetCategory> categories) {
        this.categories = categories;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setDeleted() {
        setId(TYPE_REMOVED);
        setMode(Mode.REMOVED);
    }

    public double getBudget() {
        double total = 0;
        for (BudgetCategory category : getCategories()) {
            total += category.getBudgeted();
        }
        return total;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
