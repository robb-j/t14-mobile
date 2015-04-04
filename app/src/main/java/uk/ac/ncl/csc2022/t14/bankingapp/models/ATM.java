package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 04/04/2015.
 */
public class ATM extends ModelObject{

    private String name;
    private double latitude;
    private double longitude;
    private double cost;

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
