package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 04/04/2015.
 */

/**
 * Class used to model an ATM
 */
public class ATM extends ModelObject{

    private String name;
    private double latitude;
    private double longitude;
    private double cost;

    /**
     * Creates a new ATM instance
     * @param id The unique identifier for this ATM
     * @param name A friendly name for this ATM
     * @param latitude The latitude of this ATM
     * @param longitude The longitude of this ATM
     * @param cost The cost of using this ATM
     */
    public ATM(int id, String name, double latitude, double longitude, double cost) {
        super(id);
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
        setCost(cost);
    }

    public String getName() {
        return name;
    }
    private void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }
    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getCost() {
        return cost;
    }
    private void setCost(double cost) {
        this.cost = cost;
    }
}
