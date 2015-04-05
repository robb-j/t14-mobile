package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 06/03/2015.
 */

/**
 * Class used to model a Reward a user can redeem with points
 */
public class Reward extends ModelObject {

    private String name;
    private String description;
    private int cost;

    /**
     * Creates a new Reward instance
     * @param id The unique identifier for this Reward
     * @param name A friendly name for this Reward
     * @param description A more in-depth description of this Reward
     * @param cost The cost, in points, of redeeming this Reward
     */
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
