package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by rob on 06/04/15.
 */
public class ResponseParser {

    private ModelParser mp;

    public ResponseParser() {

        mp = new ModelParser();
    }



    /*
        V1 Responses
     */
    public boolean parseLogin(JSONObject responseJson) {

        try {

            // Get the token
            String token = responseJson.getString("Token");


            // Parse the user
            User user = mp.parseUser(responseJson.getJSONObject("User"));


            // Products
            JSONArray newProdsJson = responseJson.getJSONArray("NewProducts");
            List<Product> allProducts = new ArrayList<>();
            for (int i = 0; i < newProdsJson.length(); i++) {

                allProducts.add(mp.parseProduct(newProdsJson.getJSONObject(i)));
            }

            // Rewards
            JSONArray allRewardsJson = responseJson.getJSONArray("Rewards");
            List<Reward> allRewards = new ArrayList<>();
            for (int i = 0; i < allRewardsJson.length(); i++) {

                allRewards.add(mp.parseReward(allRewardsJson.getJSONObject(i)));
            }


            // Accounts
            JSONArray allAccountsJson = responseJson.getJSONArray("Accounts");
            for (int i = 0; i < allAccountsJson.length(); i++) {

                user.getAccounts().add(mp.parseAccount(allAccountsJson.getJSONObject(i)));
            }


            // Categories
            JSONArray allCategoryJson = responseJson.getJSONArray("Categories");
            Map<Integer, BudgetCategory> allCategories = new HashMap<>();
            for (int i = 0; i < allCategoryJson.length(); i++) {

                BudgetCategory category = mp.parseCategory(allCategoryJson.getJSONObject(i));
                allCategories.put(category.getId(), category);
            }


            // Groups
            JSONArray allGroupsJson = responseJson.getJSONArray("Groups");
            for (int i = 0; i < allGroupsJson.length(); i++) {

                BudgetGroup group = mp.parseGroup(allGroupsJson.getJSONObject(i), allCategories);

                user.getAllGroups().add(group);
            }

            // Recent Points
            JSONArray allPointsJson = responseJson.getJSONArray("RecentGains");
            for (int i = 0; i < allPointsJson.length(); i++) {

                user.getRecentPoints().add(mp.parsePointGain(allPointsJson.getJSONObject(i)));
            }

            // Recent Rewards
            JSONArray allRewardsTakenJson = responseJson.getJSONArray("RecentRewards");
            for (int i = 0; i < allRewardsTakenJson.length(); i++) {

                user.getRecentRewards().add(mp.parseRewardTaken(allRewardsTakenJson.getJSONObject(i), allRewards));
            }


            // Return by giving data to the DataStore
            DataStore.sharedInstance().setCurrentUser(user);
            DataStore.sharedInstance().setProducts(allProducts);
            DataStore.sharedInstance().setRewards(allRewards);
            DataStore.sharedInstance().setToken(token);

            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }

    public boolean parseLoadTransactions(JSONObject responseJson, Account account, List<Transaction> allTransactions) {

        try {

            // Update the balance
            JSONObject accountJSON = responseJson.getJSONObject("Account");
            account.setBalance(accountJSON.getDouble("Balance"));


            // Parse the transactions
            JSONArray allTransactionsJson = responseJson.getJSONArray("Transactions");
            for (int i = 0; i < allTransactionsJson.length(); i++) {

                allTransactions.add(mp.parseTransaction(allTransactionsJson.getJSONObject(i), account));
            }

            return true;
        }
        catch (JSONException e) {
            return false;
        }
        catch (ParseException e) {
            return false;
        }
    }

    public boolean parseMakeTransfer(JSONObject responseJson, Account accountA, Account accountB) {

        try {

            // Get both accounts
            JSONObject fromAccountJson = responseJson.getJSONObject("payerAcc");
            JSONObject toAccountJson = responseJson.getJSONObject("payeeAcc");

            int fromId = fromAccountJson.getInt("ID");
            int toID = toAccountJson.getInt("ID");


            // Check the IDs
            if (accountA.getId() == fromId && accountB.getId() == toID) {


                // Update their balances
                accountA.setBalance(fromAccountJson.getDouble("Balance"));
                accountB.setBalance(toAccountJson.getDouble("Balance"));

                return true;
            }
        }
        catch (JSONException e) {}

        return false;
    }

    public boolean parseLogout(JSONObject responseJson) {


        DataStore.sharedInstance().setCurrentUser(null);
        DataStore.sharedInstance().setToken(null);
        DataStore.sharedInstance().setRewards(new ArrayList<Reward>());
        DataStore.sharedInstance().setProducts(new ArrayList<Product>());

        return true;
    }



    /*
        V2 Responses
     */
    public boolean parseNewPayments(JSONObject responseJson, List<Transaction> allTransactions) {

        try {

            // Get all the User's Accounts
            List<Account> allAccounts = DataStore.sharedInstance().getCurrentUser().getAccounts();


            // Get & parse the Transactions
            JSONArray allTransactionJson = responseJson.getJSONArray("transactions");
            for (int i = 0; i < allTransactionJson.length(); i++) {

                allTransactions.add(mp.parseTransaction(allTransactionJson.getJSONObject(i), allAccounts));
            }

            return true;
        }
        catch (JSONException e) {}
        catch (ParseException e) {}
        catch (Exception e) {}

        return false;
    }

    public boolean parseCategorisation(JSONObject responseJson) {

        try {

            // Gather up the User's Categories for updating
            Map<Integer, BudgetCategory> allCategories = new HashMap<>();
            for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {

                for (BudgetCategory category : group.getCategories()) {

                    allCategories.put(category.getId(), category);
                }
            }


            // Get & parse the JSON
            JSONArray changedJson = responseJson.getJSONArray("changedCategorys");

            for (int i = 0; i < changedJson.length(); i++) {

                JSONObject changeJson = changedJson.getJSONObject(i);
                BudgetCategory cat = allCategories.get(changeJson.getInt("ID"));
                cat.setSpent(changeJson.getDouble("Balance"));
            }

            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }

    public boolean parseChooseReward(JSONObject responseJson) {

        try {

            // Get the RewardTaken json
            JSONObject takenJson = responseJson.getJSONObject("rewardTaken");


            // Parse it
            List<Reward> rewards = DataStore.sharedInstance().getRewards();
            User user = DataStore.sharedInstance().getCurrentUser();
            user.getRecentRewards().add(mp.parseRewardTaken(takenJson, rewards));

            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }

    public int parsePerformSpin(JSONObject responseJson) {

        try {

            // Parse the number of points
            int numPoints = responseJson.getInt("points");
            User user = DataStore.sharedInstance().getCurrentUser();
            user.setPoints(numPoints);


            // Return it
            return numPoints;
        }
        catch (JSONException e) {

            return -1;
        }
    }

    public boolean parseUpdateBudget(JSONObject responseJson) {

        try {


            // Get the categroy & group json
            JSONArray groupsJson = responseJson.getJSONArray("Groups");
            JSONArray categoriesJson = responseJson.getJSONArray("Categories");


            // Create a Map to store all the categoies, by id
            Map<Integer, BudgetCategory> allCategories = new HashMap<>();


            // Parse the categories into the Map
            for (int i = 0; i < categoriesJson.length(); i++) {

                BudgetCategory category = mp.parseCategory(categoriesJson.getJSONObject(i));
                allCategories.put(category.getId(), category);
            }


            // Parse the groups & use the map to fill their relation
            List<BudgetGroup> allGroups = new ArrayList<>();
            for (int i = 0; i < groupsJson.length(); i++) {

                BudgetGroup group = mp.parseGroup(groupsJson.getJSONObject(i), allCategories);
                allGroups.add(group);
            }

            // Set the groups back onto the User
            DataStore.sharedInstance().getCurrentUser().setAllGroups(allGroups);


            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }



    /*
        V3 Responses
     */
    public boolean parseLoadATMs(JSONObject responseJson, List<ATM> allAtms) {

        try {

            // Parse the ATMs
            JSONArray allATMJson = responseJson.getJSONArray("ATMs");
            for (int i = 0; i < allATMJson.length(); i++) {

                allAtms.add(mp.parseATM(allATMJson.getJSONObject(i)));
            }

            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }

    public boolean parseLoadHeatPoints(JSONObject responseJson, List<HeatPoint> allHeatPoints) {

        try {

            // Parse the points
            JSONArray allPointJson = responseJson.getJSONArray("heatMapPoints");
            for (int i = 0; i < allPointJson.length(); i++) {

                allHeatPoints.add(mp.parseHeatPoint(allPointJson.getJSONObject(i)));
            }

            return true;
        }
        catch (JSONException e) {

            return false;
        }
    }



}
