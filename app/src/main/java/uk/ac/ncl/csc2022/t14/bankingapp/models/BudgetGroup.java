package uk.ac.ncl.csc2022.t14.bankingapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 06/03/2015.
 */
public class BudgetGroup extends ModelObject {

    private String name;
    private List<BudgetCategory> categories;

    public BudgetGroup(int id, String name) {
        super(id);
        setName(name);
        categories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }

    private void setCategories(List<BudgetCategory> categories) {
        this.categories = categories;
    }
}
