package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;

/**
 * Created by Jack on 05/04/2015.
 */
public interface HeatMapDelegate {

    void loadHeatMapPassed(List<HeatPoint> allHeatPoints);

    void loadHeatMapFailed(String errMessage);
}
