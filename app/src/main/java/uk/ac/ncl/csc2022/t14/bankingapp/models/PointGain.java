package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 06/03/2015.
 */

/**
 * Class used to model an increase in a user's points.
 */
public class PointGain extends ModelObject {

    private String name;
    private String description;
    private int points;

    /**
     * Creates a new PointGain instance
     * @param id The unique identifier for this PointGain
     * @param name A friendly name for this PointGain
     * @param description A more in-depth description of this PointGain
     * @param points The amount of points this PointGain will give
     */
    public PointGain(int id, String name, String description, int points) {
        super(id);
        setName(name);
        setDescription(description);
        setPoints(points);
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

    public int getPoints() {
        return points;
    }
    private void setPoints(int points) {
        this.points = points;
    }
}
