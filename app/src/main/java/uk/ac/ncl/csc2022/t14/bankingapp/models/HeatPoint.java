package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 04/04/2015.
 */

/**
 * Class used to model a single point of interest on a heat map. Collectively will give an idea of spending areas.
 */
public class HeatPoint {

    private double latitude;
    private double longitude;
    private int radius;

    /**
     * Creates a new HeatPoint instance
     * @param latitude The latitude on the map where this HeatPoint should be placed
     * @param longitude The longitude on the map where this HeatPoint should be placed
     * @param radius The radius of this HeatPoint, more spending in one area will mean a larger radius
     */
    public HeatPoint(double latitude, double longitude, int radius) {
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
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

    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
}
