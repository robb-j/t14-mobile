package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

/**
 * Created by Sam on 17/02/2015.
 */
public interface TransactionDelegate {

    void transactionsLoaded(Account account, List<Transaction> transactions);

    void transactionsLoadFailed(String errMessage);
}
