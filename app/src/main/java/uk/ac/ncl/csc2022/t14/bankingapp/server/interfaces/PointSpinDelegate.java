package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

/**
 * Created by Jack on 12/03/2015.
 */
public interface PointSpinDelegate {

    void spinPassed(int numPoints);

    void spinFailed(String errMessage);
}
