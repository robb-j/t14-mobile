package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.BankingApp;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.LoginActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ATMDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.HeatMapDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LogoutDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONFetcher;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONTaskDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONTaskStatus;

/**
 * An implementation of the server interfaces that connects to the live server.
 * This object handles asynchronous requests using the delegation pattern, whereby a
 * delegate must be passed to methods and that delegate will be notified when the request
 * is completed.
 * Created by Rob A on 30/03/15.
 */
public class LiveServerConnector implements ServerInterface {


    // Constants
    private final String DEFAULT_BASE_URL = "http://t14.veotest.co.uk/bankapi/";
    private final String PARAM_KEY_EDIT = "edit";
    private final String PARAM_KEY_CREATE = "create";
    private final String PARAM_KEY_DELETE = "delete";


    // Private properties
    private JSONFetcher jsonFetcher;
    private ResponseParser responseParser;
    private ProgressDialog loadingSpinner;


    /**
     * Creates a new LiveConnector, by default conneccts to DEFAULT_BASE_URL
     * For testing set the base to JSONFetcher.TEST_MODE_BASEURL which will force it
     * to load local files.
     */
    public LiveServerConnector() {

        setBaseUrl(DEFAULT_BASE_URL);
        responseParser = new ResponseParser();
    }

    public void setBaseUrl(String baseUrl) {

        jsonFetcher = new JSONFetcher(baseUrl);
    }


    /** Creates the params needed for all POSTs (except login) */
    private List<NameValuePair> baseParams() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userID", "" + DataStore.sharedInstance().getCurrentUser().getId()));
        params.add(new BasicNameValuePair("token", DataStore.sharedInstance().getToken()));

        return params;
    }

    /** A common method to add a loading popup when performing a server task */
    private void addLoadingSpinner(String title, String message) {

        // Only show if not testing
        if ( ! jsonFetcher.isTesting()) {

            // Create and add a progress dialog
            loadingSpinner = new ProgressDialog(BankingApp.getContext());
            loadingSpinner.setTitle(title);
            loadingSpinner.setMessage(message);
            if (!loadingSpinner.isShowing())
                loadingSpinner.show();
        }
    }

    /** A common method to remove the loading spinner after each server task */
    private void removeLoadingSpinner() {

        // We only need to hide it if we aren't testing and its showing
        if ( ! jsonFetcher.isTesting()) {
            if (loadingSpinner != null && loadingSpinner.isShowing()) {
                loadingSpinner.dismiss();
            }
        }
    }


    private void forceLogout(String message) {

        // Reset the DataStore
        DataStore.sharedInstance().setCurrentUser(null);
        DataStore.sharedInstance().setNewProducts(new ArrayList<Product>());
        DataStore.sharedInstance().setToken("");
        DataStore.sharedInstance().setRewards(new ArrayList<Reward>());


        // Return the user to the login screen
        Intent intent = new Intent(BankingApp.getContext(), LoginActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        BankingApp.getContext().startActivity(mainIntent);


        // Inform the user with a Toast
        Toast.makeText(BankingApp.getContext(), "Logged Out: " + message, Toast.LENGTH_LONG).show();
    }



    /*
        V1
     */
    @Override
    public void login(String username, char[] password, int[] indices, final LoginDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Logging in", "Please wait...");


        // Fail if there aren't 3 indices
        if (indices.length != 3) {

            delegate.loginFailed("Password not entered correctly");
        }


        // Create the parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", new String(password)));
        params.add(new BasicNameValuePair("index1", "" + indices[0]));
        params.add(new BasicNameValuePair("index2", "" + indices[1]));
        params.add(new BasicNameValuePair("index3", "" + indices[2]));


        // Create the delegate to respond to the fetch
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();


                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse response
                if (status == JSONTaskStatus.PASSED && responseParser.parseLogin(json)) {

                    // If parsed, inform the delegate
                    delegate.loginPassed(DataStore.sharedInstance().getCurrentUser());
                } else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.loginFailed(message);
                }


            }
        };


        // Perform the fetch
        jsonFetcher.performFetch("login", params, taskDelegate);
    }

    @Override
    public void loadTransactions(final Account account, int month, int year, final TransactionDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Loading Transactions", "Please wait...");


        // Create the POST params
        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("month", "" + month));
        params.add(new BasicNameValuePair("year", "" + year));
        params.add(new BasicNameValuePair("accountID", "" + account.getId()));


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();

                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the transactions
                List<Transaction> transations = new ArrayList<>();
                if (status == JSONTaskStatus.PASSED && responseParser.parseLoadTransactions(json, account, transations)) {

                    // If successful pass them back to the delegate
                    delegate.transactionsLoaded(account, transations);
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.transactionsLoadFailed(message);
                }
            }
        };


        // Fetch the json
        jsonFetcher.performFetch("loadTransactions", params, taskDelegate);
    }

    @Override
    public void makeTransfer(final Account accountA, final Account accountB, final double amount, final TransferDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Making Transfer", "Please wait...");


        // Create the POST params
        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("accountAID", "" + accountA.getId()));
        params.add(new BasicNameValuePair("accountBID", "" + accountB.getId()));
        params.add(new BasicNameValuePair("amount", "" + amount));


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();


                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                if (status == JSONTaskStatus.PASSED && responseParser.parseMakeTransfer(json, accountA, accountB)) {

                    // If parsed, inform the delegate
                    delegate.transferPassed(accountA, accountB, amount);
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.transferFailed(message);
                }
            }
        };


        // Fetch the json
        jsonFetcher.performFetch("makeTransfer", params, taskDelegate);
    }

    @Override
    public void logout(final LogoutDelegate delegate) {

        // Create the params for a POST request
        List<NameValuePair> params = baseParams();


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {


                // Attempt to parse the response
                if (status == JSONTaskStatus.PASSED && responseParser.parseLogout(json)) {

                    // If parsed, inform the delegate
                    delegate.logoutPassed();
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.logoutFailed(message);
                }
            }
        };


        // Fetch the json
        jsonFetcher.performFetch("logout", params, taskDelegate);
    }




    /*
        V2
     */
    @Override
    public void loadNewPaymentsForUser(final NewPaymentsDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Loading New Payments", "Please wait...");


        // Create the POST params
        List<NameValuePair> params = baseParams();


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the transactions
                List<Transaction> transations = new ArrayList<>();
                if (status == JSONTaskStatus.PASSED && responseParser.parseNewPayments(json, transations)) {

                    // If parsed, pass them to the delegate
                    delegate.newPaymentsLoaded(transations);
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.newPaymentsLoadFailed(message);
                }
            }
        };


        // Fetch the delegate
        jsonFetcher.performFetch("newPayments", params, taskDelegate);
    }

    @Override
    public void categorisePayments(List<Categorisation> categorizations, final CategoriseDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Categorising Payments", "Please wait...");


        // Create the POST parameters
        List<NameValuePair> params = baseParams();
        for (Categorisation ctgr : categorizations) {

            int catID = ctgr.getBudgetCategory() == null? -1 : ctgr.getBudgetCategory().getId();
            int tranID = ctgr.getTransaction().getId();
            params.add(new BasicNameValuePair("categories[" + tranID + "]", "" + catID));
        }


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                ObjectHolder<Boolean> hasNewSpin = new ObjectHolder<>(false);
                if (status == JSONTaskStatus.PASSED && responseParser.parseCategorisation(json, hasNewSpin) && hasNewSpin.getValue() != null) {

                    // If successful tell the delegate
                    delegate.categorisationPassed(hasNewSpin.getValue());
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.categorisationFailed(message);
                }
            }
        };


        // Fetch the json
        jsonFetcher.performFetch("categorisePayments", params, taskDelegate);
    }

    @Override
    public void updateBudget(List<BudgetGroup> newBudget, final BudgetUpdateDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Updating Budget", "Please wait...");


        // Params
        List<NameValuePair> params = baseParams();

        String paramBase = "groups";

        // Loop through groups
        int numGroups = 0;
        for (BudgetGroup group : newBudget) {

            String groupBase = paramBase + "[" + numGroups + "]";


            // If delete
            if (group.getMode() == BudgetGroup.Mode.REMOVED) {

                // Add the id & tell it to delete
                params.add(new BasicNameValuePair(groupBase + "[id]", "" + group.getId()));
                params.add(new BasicNameValuePair(groupBase + "[mode]", PARAM_KEY_DELETE));
                numGroups++;
            }


            // If editing
            if (group.getMode() == BudgetGroup.Mode.EDITED) {

                // Add the id and tell it to edit
                params.add(new BasicNameValuePair(groupBase + "[id]", "" + group.getId()));
                params.add(new BasicNameValuePair(groupBase + "[mode]", PARAM_KEY_EDIT));
                params.add(new BasicNameValuePair(groupBase + "[title]", group.getName()));
                numGroups++;
            }


            // If creating
            if (group.getMode() == BudgetGroup.Mode.NEW) {

                // Tell it to create a group
                params.add(new BasicNameValuePair(groupBase + "[mode]", PARAM_KEY_CREATE));
                params.add(new BasicNameValuePair(groupBase + "[title]", group.getName()));
                numGroups++;
            }


            // If not deleting and changing something
            if (group.getMode() != BudgetGroup.Mode.REMOVED && group.getMode() != BudgetGroup.Mode.UNCHANGED) {

                // Loop through the categories on the group
                int numCategories = 0;
                for (BudgetCategory category : group.getCategories()) {

                    // Get the category & setup the new param base
                    String categoryBase = groupBase + "[categories][" + numCategories + "]";

                    // If deleting
                    if (category.getMode() == BudgetCategory.Mode.REMOVED) {

                        // Add the id & tell it to delete
                        params.add(new BasicNameValuePair(categoryBase + "[id]", "" + category.getId()));
                        params.add(new BasicNameValuePair(categoryBase + "[mode]", PARAM_KEY_DELETE));
                        numCategories++;
                    }

                    // If creating
                    if (category.getMode() == BudgetCategory.Mode.NEW) {

                        // Add the title & budget and tell it to create
                        params.add(new BasicNameValuePair(categoryBase + "[mode]", PARAM_KEY_CREATE));
                        params.add(new BasicNameValuePair(categoryBase + "[title]", category.getName()));
                        params.add(new BasicNameValuePair(categoryBase + "[budget]", "" + category.getBudgeted()));
                        numCategories++;
                    }

                    // If editing
                    if (category.getMode() == BudgetCategory.Mode.EDITED) {

                        // Add the id and properties and tell it to update
                        params.add(new BasicNameValuePair(categoryBase + "[id]", "" + category.getId()));
                        params.add(new BasicNameValuePair(categoryBase + "[mode]", PARAM_KEY_EDIT));
                        params.add(new BasicNameValuePair(categoryBase + "[title]", category.getName()));
                        params.add(new BasicNameValuePair(categoryBase + "[budget]", "" + category.getBudgeted()));
                        numCategories++;
                    }
                }
            }
        }

        // If there aren't any extra params, theres no need to submit th data
        if (params.size() < 3) {

            removeLoadingSpinner();
            delegate.updateBudgetPassed();
            return;
        }


        // Delegate
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                if (status == JSONTaskStatus.PASSED && responseParser.parseUpdateBudget(json)) {

                    // If successful, inform the delegate
                    delegate.updateBudgetPassed();
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.updateBudgetFailed(message);
                }
            }
        };


        // Call the fetcher
        jsonFetcher.performFetch("updateBudget", params, taskDelegate);
    }

    @Override
    public void chooseReward(Reward reward, final ChooseRewardDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Choosing Reward", "Please wait...");


        // Create the POST params
        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("rewardID", "" + reward.getId()));


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                if (status == JSONTaskStatus.PASSED && responseParser.parseChooseReward(json)) {

                    // If succesful inform the delegate
                    delegate.chooseRewardPassed();
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.chooseRewardFailed(message);
                }
            }
        };


        // Perform the fetch
        jsonFetcher.performFetch("chooseReward", params, taskDelegate);
    }

    @Override
    public void performSpin(final PointSpinDelegate delegate) {

        // Add the loading spinner
        addLoadingSpinner("Loading", "Please wait...");


        // Create the POST params
        List<NameValuePair> params = baseParams();


        // Create the delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {

                // Remove the loading spinner
                removeLoadingSpinner();



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                ObjectHolder<Integer> numPoints = new ObjectHolder<>();
                if (status == JSONTaskStatus.PASSED && responseParser.parsePerformSpin(json, numPoints) && numPoints.getValue() != null) {

                    // If successful, inform the delegate and pass the number of points
                    delegate.spinPassed(numPoints.getValue());
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.spinFailed(message);
                }
            }
        };


        // Fetch the json
        jsonFetcher.performFetch("performSpin", params, taskDelegate);
    }




    /*
        V3
     */
    @Override
    public void loadHeatMap(int[] accounts, Date start, Date end, final HeatMapDelegate delegate) {

        //Create POST params
        List<NameValuePair> params = baseParams();


        // Create delegate to respond to POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the response
                List<HeatPoint> allPoints = new ArrayList<>();
                if (status == JSONTaskStatus.PASSED && responseParser.parseLoadHeatPoints(json, allPoints)) {

                    // If successful, pass them to the delegate
                    delegate.loadHeatMapPassed(allPoints);
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.loadHeatMapFailed(message);
                }
            }
        };


        // Perform the fetch
        jsonFetcher.performFetch("loadHeatMap", params, taskDelegate);
    }

    @Override
    public void loadATMS(final ATMDelegate delegate) {

        // Create the POST params
        List<NameValuePair> params = baseParams();


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(JSONTaskStatus status, String message, JSONObject json) {



                // If logged out, log out
                if (status == JSONTaskStatus.LOGGED_OUT) {
                    forceLogout(message);
                    return;
                }


                // Attempt to parse the ATMs
                List<ATM> allAtms = new ArrayList<>();
                if (status == JSONTaskStatus.PASSED && responseParser.parseLoadATMs(json, allAtms)) {

                    // If successful, pass them to the delegate
                    delegate.loadATMsPassed(allAtms);
                }
                else {

                    // Otherwise get the error and pass that back
                    message = responseParser.parseErrorOrDefault(message);
                    delegate.loadATMsFailed(message);
                }
            }
        };

        // Perform the fetch
        jsonFetcher.performFetch("loadATMs", params, taskDelegate);
    }

}
