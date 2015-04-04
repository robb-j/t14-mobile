package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 12/03/2015.
 */
public class Categorisation extends ModelObject {

    private Transaction transaction;
    private BudgetCategory budgetCategory;
    private double latitude;
    private double longitude;

    public Categorisation(int id, double latitude, double longitude) {
        super(id);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public BudgetCategory getBudgetCategory() {
        return budgetCategory;
    }
    public void setBudgetCategory(BudgetCategory budgetCategory) {
        this.budgetCategory = budgetCategory;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
