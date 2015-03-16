package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by Sam on 17/02/2015.
 */
public interface LoginDelegate {

    void loginPassed(List<Product> products, List<Reward> rewards);

    void loginFailed(String errMessage);
}
