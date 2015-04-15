package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import android.app.ProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.BankingApp;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
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

/**
 * An implementation of the server interfaces that connects to the live server
 * Created by Rob A on 30/03/15.
 */
public class LiveServerConnector implements ServerInterface {

    private final String DEFAULT_BASE_URL = "http://t14.veotest.co.uk/bankapi/";
    private JSONFetcher jsonFetcher;
    private ResponseParser responseParser;
    private ProgressDialog loadingSpinner;


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

    private void addLoadingSpinner(String title, String message) {
        loadingSpinner = new ProgressDialog(BankingApp.getContext());
        loadingSpinner.setTitle(title);
        loadingSpinner.setMessage(message);
        if (!loadingSpinner.isShowing())
            loadingSpinner.show();
    }

    private void removeLoadingSpinner() {
        if (loadingSpinner.isShowing()) {
            loadingSpinner.dismiss();
        }
    }




    /*
        V1
     */
    @Override
    public void login(String username, char[] password, int[] indices, final LoginDelegate delegate) {
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
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

            // Attempt to parse response
            if (success && responseParser.parseLogin(json)) {

                delegate.loginPassed(DataStore.sharedInstance().getCurrentUser());
            } else {

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

        addLoadingSpinner("Loading Transactions", "Please wait...");

        // Create the POST params
        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("month", "" + month));
        params.add(new BasicNameValuePair("year", "" + year));
        params.add(new BasicNameValuePair("accountID", "" + account.getId()));


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

            List<Transaction> transations = new ArrayList<>();
            if (success && responseParser.parseLoadTransactions(json, account, transations)) {

                delegate.transactionsLoaded(account, transations);
            }
            else {

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

        addLoadingSpinner("Making Transfer", "Please wait...");

        // Create the POST params
        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("accountAID", "" + accountA.getId()));
        params.add(new BasicNameValuePair("accountBID", "" + accountB.getId()));
        params.add(new BasicNameValuePair("amount", "" + amount));


        // Create a delegate to respond to the POST
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

                if (success && responseParser.parseMakeTransfer(json, accountA, accountB)) {
                    delegate.transferPassed(accountA, accountB, amount);
                }
                else {

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
            public void taskCompleted(boolean success, String message, JSONObject json) {

                if (success && responseParser.parseLogout(json)) {

                    delegate.logoutPassed();
                }
                else {

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

        addLoadingSpinner("Loading New Payments", "Please wait...");

        List<NameValuePair> params = baseParams();

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

                List<Transaction> transations = new ArrayList<>();
                if (success && responseParser.parseNewPayments(json, transations)) {

                    delegate.newPaymentsLoaded(transations);
                }
                else {

                    message = responseParser.parseErrorOrDefault(message);
                    delegate.newPaymentsLoadFailed(message);
                }
            }
        };

        jsonFetcher.performFetch("newPayments", params, taskDelegate);
    }

    @Override
    public void categorisePayments(List<Categorisation> categorizations, final CategoriseDelegate delegate) {

        addLoadingSpinner("Categorising Payments", "Please wait...");

        List<NameValuePair> params = baseParams();
        for (Categorisation ctgr : categorizations) {

            int catID = ctgr.getBudgetCategory().getId();
            int tranID = ctgr.getTransaction().getId();
            params.add(new BasicNameValuePair("categories[" + tranID + "]", "" + catID));
        }

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

            if (success && responseParser.parseCategorisation(json)) {

                delegate.categorisationPassed(false);
            }
            else {

                message = responseParser.parseErrorOrDefault(message);
                delegate.categorisationFailed(message);
            }
            }
        };

        jsonFetcher.performFetch("categorisePayments", params, taskDelegate);
    }

    @Override
    public void updateBudget(List<BudgetGroup> newBudget, final BudgetUpdateDelegate delegate) {

        // addLoadingSpinner("Updating Budget", "Please wait...");

        // Params
        List<NameValuePair> params = baseParams();

        String paramBase = "groups";

        // Loop through groups
        for (int i = 0; i < newBudget.size(); i++) {

            BudgetGroup group = newBudget.get(i);
            String groupBase = paramBase + "[" + i + "]";

            // Add group params
            String mode = "";

            // If delete
            if (false) {

                params.add(new BasicNameValuePair(groupBase + "[mode]", "delete"));
                params.add(new BasicNameValuePair(groupBase + "[id]", "" + group.getId()));
            }

            // If editing
            if (false) {

                params.add(new BasicNameValuePair(groupBase + "[id]", "" + group.getId()));
                params.add(new BasicNameValuePair(groupBase + "[mode]", "edit"));
            }

            // If creating
            if (false) {

                params.add(new BasicNameValuePair(groupBase + "[mode]", "create"));
            }

            // If not deleting
            if (false) {

                // Loop through the categories
                for (int j = 0; j < group.getCategories().size(); j++) {

                    BudgetCategory categroy = group.getCategories().get(j);

                    String categoryBase = groupBase + "[" + j + "]";

                    // If deleting
                    if (false) {

                        params.add(new BasicNameValuePair("", ""));
                    }

                    // If creating
                    if (false) {

                    }

                    // If editing
                    if (false) {

                    }
                }
            }
        }


        // Delegate
        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                // removeLoadingSpinner();

                if (success && responseParser.parseUpdateBudget(json)) {

                    delegate.updateBudgetPassed();
                }
                else {

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

        addLoadingSpinner("Choosing Reward", "Please wait...");

        List<NameValuePair> params = baseParams();
        params.add(new BasicNameValuePair("rewardID", "" + reward.getId()));

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

                if (success && responseParser.parseChooseReward(json)) {

                    delegate.chooseRewardPassed();
                }
                else {

                    message = responseParser.parseErrorOrDefault(message);
                    delegate.chooseRewardFailed(message);
                }
            }
        };

        jsonFetcher.performFetch("chooseReward", params, taskDelegate);
    }

    @Override
    public void performSpin(final PointSpinDelegate delegate) {

        addLoadingSpinner("Loading", "Please wait...");

        List<NameValuePair> params = baseParams();

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                removeLoadingSpinner();

                if (success) {
                    int numPoints = responseParser.parsePerformSpin(json);

                    if (numPoints > 0) {

                        delegate.spinPassed(numPoints);
                        return;
                    }
                }

                message = responseParser.parseErrorOrDefault(message);
                delegate.spinFailed(message);

            }
        };

        jsonFetcher.performFetch("performSpin", params, taskDelegate);
    }




    /*
        V3
     */
    @Override
    public void loadHeatMap(int[] accounts, Date start, Date end, final HeatMapDelegate delegate) {

        List<NameValuePair> params = baseParams();

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                List<HeatPoint> allPoints = new ArrayList<>();
                if (success && responseParser.parseLoadHeatPoints(json, allPoints)) {

                    delegate.loadHeatMapPassed(allPoints);
                }
                else {

                    message = responseParser.parseErrorOrDefault(message);
                    delegate.loadHeatMapFailed(message);
                }
            }
        };

        jsonFetcher.performFetch("loadHeatMap", params, taskDelegate);
    }

    @Override
    public void loadATMS(final ATMDelegate delegate) {

        List<NameValuePair> params = baseParams();

        JSONTaskDelegate taskDelegate = new JSONTaskDelegate() {
            @Override
            public void taskCompleted(boolean success, String message, JSONObject json) {

                List<ATM> allAtms = new ArrayList<>();
                if (success && responseParser.parseLoadATMs(json, allAtms)) {

                    delegate.loadATMsPassed(allAtms);
                }
                else {

                    message = responseParser.parseErrorOrDefault(message);
                    delegate.loadATMsFailed(message);
                }
            }
        };

        jsonFetcher.performFetch("loadATMs", params, taskDelegate);
    }

}
