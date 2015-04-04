package uk.ac.ncl.csc2022.t14.bankingapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 06/03/2015.
 */
public class MonthBudget extends ModelObject {

    /** Maps Category ID to amount budgeted this month */
    private Map<Integer, Double> budgetAmount;
    /** Maps Category ID to category's new name */
    private  Map<Integer,String> categoryNameChanges;
    /** Maps Group ID to group's new name */
    private Map<Integer, String> groupNameChanges;

    public List<BudgetGroup> newGroups;

    public MonthBudget(int id) {
        super(id);
        budgetAmount = new HashMap<>();
        categoryNameChanges = new HashMap<>();
        groupNameChanges = new HashMap<>();
        newGroups = new ArrayList<>();
    }

    public Map<Integer, Double> getBudgetAmount() {
        return budgetAmount;
    }
    private void setBudgetAmount(Map<Integer, Double> budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Map<Integer, String> getCategoryNameChanges() {
        return categoryNameChanges;
    }
    private void setCategoryNameChanges(Map<Integer, String> categoryNameChanges) {
        this.categoryNameChanges = categoryNameChanges;
    }

    public Map<Integer, String> getGroupNameChanges() {
        return groupNameChanges;
    }
    private void setGroupNameChanges(Map<Integer, String> groupNameChanges) {
        this.groupNameChanges = groupNameChanges;
    }

    public List<BudgetGroup> getNewGroups() {
        return newGroups;
    }

    public void setNewGroups(List<BudgetGroup> newGroups) {
        this.newGroups = newGroups;
    }
}
