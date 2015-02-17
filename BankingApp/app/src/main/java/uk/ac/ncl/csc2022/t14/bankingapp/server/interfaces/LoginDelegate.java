package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by Sam on 17/02/2015.
 */
public interface LoginDelegate {

    void loginPassed(User user, List<Product> products, String token);

    void loginFailed(String errMessage);
}
