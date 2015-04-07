package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

/**
 * A Singleton object for getting things from the server
 * Created by Jack on 02/03/2015.
 */
public class DataStore {

    private static final DataStore sharedInstance = new DataStore();
    private User currentUser;
    private List<Product> products;
    private List<Reward> rewards;
    private String token;
    private ServerInterface serverConnector;



    /** Gets a shared instance of this object */
    public static DataStore sharedInstance() {
        return sharedInstance;
    }



    /** Make the constructor private to enforce the singleton */
    private DataStore() {
        serverConnector = new DummyServerConnector();
    }



    // The current user
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }



    // The Products available to the User
    public void setProducts(List<Product> product) {
        products = product;
    }
    public List<Product> getProducts() {return products;}



    // The Rewards available to the User
    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }



    // The Token
    public void setToken(String Token) {
        token = Token;
    }

    public String getToken(){
        return token;
    }



    // The ServerConnector
    public void setServerConnector(ServerInterface connector) {
        serverConnector = connector;
    }

    public ServerInterface getConnector() {
        return serverConnector;
    }
}
