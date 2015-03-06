package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 06/03/2015.
 */
public class Reward extends ModelObject {

    private String name;
    private String description;
    private int cost;

    public Reward(int id, String name, String description, int cost) {
        super(id);
        setName(name);
        setDescription(description);
        setCost(cost);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    private void setCost(int cost) {
        this.cost = cost;
    }
}
