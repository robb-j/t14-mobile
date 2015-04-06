package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by Jack on 02/03/2015.
 */
public class DataStore {

    private static final DataStore sharedInstance = new DataStore();
    private User currentUser;
    private List<Product> products;
    private List<Reward> rewards;
    private String token;


    public static DataStore sharedInstance() {
        return sharedInstance;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public void setProducts(List<Product> product)
    {
        products = product;
    }
    public List<Product> getProducts() {return products;}

    public List<Reward> getRewards() {
        return rewards;
    }



    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setToken(String Token)
    {
        token = Token;
    }
    public String getToken(){return token;}
}
