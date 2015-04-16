package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONParser;

/**
 * An object that handles the response from a LiveServer request, converts the json to multiple model objects & sets relations
 * Created by rob on 06/04/15.
 */
public class ResponseParser {

    private ModelParser mp;
    private String parsingError = null;

    public ResponseParser() {

        // Create a parser to convert json to ModelObjects
        mp = new ModelParser();
        parsingError = null;
    }

    public String parseErrorOrDefault(String defaultValue) {

        return parsingError != null ? parsingError : defaultValue;
    }



    /*
        V1 Responses
     */
    public boolean parseLogin(JSONObject responseJson) {

        JSONParser responseParser = new JSONParser(responseJson);

        // Get the token
        String token = responseParser.getString("Token", null);

        if (token == null) {

            // Pass message
            parsingError = "No Token Provided";
            return false;
        }


        // Parse the user
        User user = mp.parseUser(responseParser.getJSONObject("User"));
        user.setNumNewPayments(responseParser.getInt("NumNewPayments"));


        // Products
        List<JSONObject> prodsJson = responseParser.getJSONObjectList("AllProducts");
        Map<Integer, Product> allProducts = new HashMap<>();
        for (JSONObject productParser : prodsJson) {

            Product p = mp.parseProduct(productParser);
            allProducts.put(p.getId(), p);
        }

        // New Products
        List<Integer> newProdIds = responseParser.getIntegerList("NewProducts");
        List<Product> newProducts = new ArrayList<>();
        for (Integer i : newProdIds) {

           newProducts.add(allProducts.get(i));
        }

        // Rewards
        List<JSONObject> allRewardsJson = responseParser.getJSONObjectList("Rewards");
        List<Reward> allRewards = new ArrayList<>();
        for (JSONObject rewardJson : allRewardsJson) {

            allRewards.add(mp.parseReward(rewardJson));
        }


        // Accounts
        List<JSONObject> allAccountsJson = responseParser.getJSONObjectList("Accounts");
        for (JSONObject accountJson : allAccountsJson) {

            user.getAccounts().add(mp.parseAccount(accountJson, allProducts));
        }


        // Categories
        List<JSONObject> allCategoryJson = responseParser.getJSONObjectList("Categories");
        Map<Integer, BudgetCategory> allCategories = new HashMap<>();
        for (JSONObject categroyJson : allCategoryJson) {

            BudgetCategory category = mp.parseCategory(categroyJson);
            allCategories.put(category.getId(), category);
        }


        // Groups
        List<JSONObject> allGroupsJson = responseParser.getJSONObjectList("Groups");
        for (JSONObject groupJson : allGroupsJson) {

            BudgetGroup group = mp.parseGroup(groupJson, allCategories);
            user.getAllGroups().add(group);
        }

        // Recent Points
        List<JSONObject> allPointsJson = responseParser.getJSONObjectList("RecentGains");
        for (JSONObject pointJson : allPointsJson) {

            user.getRecentPoints().add(mp.parsePointGain(pointJson));
        }

        // Recent Rewards
        List<JSONObject> allRewardsTakenJson = responseParser.getJSONObjectList("RecentRewards");
        for (JSONObject rewTakenJson : allRewardsTakenJson) {

            user.getRecentRewards().add(mp.parseRewardTaken(rewTakenJson, allRewards));
        }



        // Return by giving data to the DataStore
        DataStore.sharedInstance().setCurrentUser(user);
        DataStore.sharedInstance().setProducts(new ArrayList<>(allProducts.values()));
        DataStore.sharedInstance().setNewProducts(newProducts);
        DataStore.sharedInstance().setRewards(allRewards);
        DataStore.sharedInstance().setToken(token);


        return true;
    }

    public boolean parseLoadTransactions(JSONObject responseJson, Account account, List<Transaction> allTransactions) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Update the balance
        JSONParser accountJSON = new JSONParser(responseParser.getJSONObject("Account"));
        account.setBalance(accountJSON.getDouble("Balance"));


        // Parse the transactions
        List<JSONObject> allTransactionsJson = responseParser.getJSONObjectList("Transactions");
        for (JSONObject transactionJson : allTransactionsJson) {

            allTransactions.add(mp.parseTransaction(transactionJson, account));
        }

        return true;
    }

    public boolean parseMakeTransfer(JSONObject responseJson, Account accountA, Account accountB) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Get both accounts
        JSONParser fromAccountJson = new JSONParser(responseParser.getJSONObject("payerAcc"));
        JSONParser toAccountJson = new JSONParser(responseParser.getJSONObject("payeeAcc"));

        int fromId = fromAccountJson.getInt("ID");
        int toID = toAccountJson.getInt("ID");


        // Check the IDs
        if (accountA.getId() == fromId && accountB.getId() == toID && fromId > 0 && toID > 0) {


            // Update their balances
            accountA.setBalance(fromAccountJson.getDouble("Balance"));
            accountB.setBalance(toAccountJson.getDouble("Balance"));

            return true;
        }
        return false;
    }

    public boolean parseLogout(JSONObject responseJson) {

        JSONParser responseParser = new JSONParser(responseJson);

        parsingError = responseParser.getString("Message", null);

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

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Get all the User's Accounts
        List<Account> allAccounts = DataStore.sharedInstance().getCurrentUser().getAccounts();


        // Get & parse the Transactions
        List<JSONObject> allTransactionJson = responseParser.getJSONObjectList("transactions");
        for (JSONObject transactionJson : allTransactionJson) {

            allTransactions.add(mp.parseTransaction(transactionJson, allAccounts));
        }


        return true;
    }

    public boolean parseCategorisation(JSONObject responseJson, ObjectHolder<Boolean> hasNewSpin) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Gather up the User's Categories for updating
        Map<Integer, BudgetCategory> allCategories = new HashMap<>();
        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {

            for (BudgetCategory category : group.getCategories()) {

                allCategories.put(category.getId(), category);
            }
        }


        // Get & parse the JSON
        List<JSONObject> changedCatJson = responseParser.getJSONObjectList("changedCategorys");

        for (JSONObject categoryJson : changedCatJson) {

            JSONParser parser = new JSONParser(categoryJson);

            BudgetCategory cat = allCategories.get(parser.getInt("ID"));
            cat.setSpent(parser.getDouble("Balance"));
        }


        int numNewPayments = responseParser.getInt("numNewPayments", -1);
        if (numNewPayments > -1) {
            DataStore.sharedInstance().getCurrentUser().setNumNewPayments(numNewPayments);
        }


        // Parse spin properties
        hasNewSpin.setValue(responseParser.getBoolean("newSpin"));
        int numSpins = responseParser.getInt("numberOfSpins", -2);
        
        if (numSpins > -1) {
            DataStore.sharedInstance().getCurrentUser().setNumberOfSpins(numSpins);
        }

        return true;

    }

    public boolean parseChooseReward(JSONObject responseJson) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Get the RewardTaken json
        JSONObject takenJson = responseParser.getJSONObject("rewardTaken");


        // Parse it
        List<Reward> rewards = DataStore.sharedInstance().getRewards();
        User user = DataStore.sharedInstance().getCurrentUser();
        user.getRecentRewards().add(mp.parseRewardTaken(takenJson, rewards));

        return true;
    }

    public boolean parsePerformSpin(JSONObject responseJson, ObjectHolder<Integer> numPoints) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Parse the number of points
        int newPoints = responseParser.getInt("newPoints");
        int totalPoints = responseParser.getInt("totalPoints", -1);
        int totSpins =  responseParser.getInt("currentSpins");
        User user = DataStore.sharedInstance().getCurrentUser();
        user.setPoints(totalPoints);
        user.setNumberOfSpins(totSpins);

        if (totalPoints > 0) {

            numPoints.setValue(newPoints);
            return true;
        }
        return false;
    }

    public boolean parseUpdateBudget(JSONObject responseJson) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Get the categroy & group json
        List<JSONObject> allGroupsJson = responseParser.getJSONObjectList("Groups");
        List<JSONObject> allCategoriesJson = responseParser.getJSONObjectList("Categories");


        // Create a Map to store all the categoies, by id
        Map<Integer, BudgetCategory> allCategories = new HashMap<>();


        // Parse the categories into the Map
        for (JSONObject categoryJson : allCategoriesJson) {

            BudgetCategory category = mp.parseCategory(categoryJson);
            allCategories.put(category.getId(), category);
        }


        // Parse the groups & use the map to fill their relation
        List<BudgetGroup> allGroups = new ArrayList<>();
        for (JSONObject groupJson : allGroupsJson) {

            BudgetGroup group = mp.parseGroup(groupJson, allCategories);
            allGroups.add(group);
        }

        // Set the groups back onto the User
        DataStore.sharedInstance().getCurrentUser().setAllGroups(allGroups);


        return true;
    }



    /*
        V3 Responses
     */
    public boolean parseLoadATMs(JSONObject responseJson, List<ATM> allAtms) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Parse the ATMs
        List<JSONObject> allATMsJson = responseParser.getJSONObjectList("ATMs");
        for (JSONObject atmJson : allATMsJson) {

            allAtms.add(mp.parseATM(atmJson));
        }

        return true;
    }

    public boolean parseLoadHeatPoints(JSONObject responseJson, List<HeatPoint> allHeatPoints) {

        // Create a parser from the response
        JSONParser responseParser = new JSONParser(responseJson);


        // Parse the points
        List<JSONObject> allPointJson = responseParser.getJSONObjectList("heatMapPoints");
        for (JSONObject pointJson : allPointJson) {

            allHeatPoints.add(mp.parseHeatPoint(pointJson));
        }

        return true;
    }



}
