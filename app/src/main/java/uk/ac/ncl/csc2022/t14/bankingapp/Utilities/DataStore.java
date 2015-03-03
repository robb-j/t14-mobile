package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by Jack on 02/03/2015.
 */
public class DataStore {

    private static final DataStore sharedInstance = new DataStore();
    private User currentUser;

    public static DataStore sharedInstance() {
        return sharedInstance;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
