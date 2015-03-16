package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

/**
 * Created by Jack on 12/03/2015.
 */
public interface NewPaymentsDelegate {

    void newPaymentsLoaded(List<Transaction> transactions);

    void newPaymentsLoadFailed(String errMessage);

}
