package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;

/**
 * Created by Jack on 05/04/2015.
 */
public interface ATMDelegate {

    void loadATMsPassed(List<ATM> allATMs);

    void loadATMsFailed(String errMessage);
}
