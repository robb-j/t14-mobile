package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * An object to convert JSON objects to useful Models
 * Created by rob on 06/04/15.
 */
public class ModelParser {

    /*
        Utilties
     */
    public Date parseDate(String rawDate) throws ParseException {

        // Format & parse the date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        return format.parse(rawDate);
    }




    /*
        V1 Parser
     */
    public User parseUser(JSONObject json) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String username = json.getString("Username");
        String first = json.getString("FirstName");
        String last = json.getString("LastName");
        String dob = json.getString("DOB");


        // Create the user
        User user = new User(id, username, first, last, dob);


        // Add extra properties
        user.setNumberOfSpins(json.getInt("NumberOfSpins"));
        user.setPoints(json.getInt("Points"));

        return user;
    }

    public Account parseAccount(JSONObject json, Map<Integer, Product> allProducts) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("AccountType");
        double balance = json.getDouble("Balance");
        double overdraft = json.getDouble("OverdraftLimit");

        // Create Account from properties
        Account account = new Account(id , name, balance, overdraft);

        account.setProduct(allProducts.get(json.getInt("Product")));

        try {
            Date firstTran = parseDate(json.getString("FirstTransaction"));
            account.setFirstTransaction(firstTran);
        }
        catch (ParseException e) {};
        return account;
    }

    public Transaction parseTransaction(JSONObject json, Account account) throws JSONException, ParseException {

        // Get properties
        int id = json.getInt("ID");
        double amount = json.getDouble("Amount");
        Date date = parseDate(json.getString("Date"));
        String payee = json.getString("Payee");


        // Create Transaction from properties
        return new Transaction(id, amount, date, account, payee);
    }

    public Transaction parseTransaction(JSONObject json, List<Account> allAccounts) throws  JSONException, ParseException, Exception {

        // Get Account relation
        int accountID = json.getInt("Account");

        for (Account a : allAccounts) {

            if (a.getId() == accountID) {

                // Create with given account
                return parseTransaction(json, a);
            }
        }
        throw new Exception("Account Not Found");
    }

    public Product parseProduct(JSONObject json) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String title = json.getString("Title");
        String content = json.getString("Content");


        // Create Product from properties
        return new Product(id, title, content);
    }




    /*
        V2 Parser
     */
    public BudgetCategory parseCategory(JSONObject json) throws  JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("Title");
        double budgeted = json.getDouble("Budgeted");
        double spent = json.getDouble("Balance");


        // Create Category from properties
        return new BudgetCategory(id, name, budgeted, spent);
    }

    public BudgetGroup parseGroup(JSONObject json, Map<Integer, BudgetCategory> allCategories) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("Title");


        // Create Group from properties
        BudgetGroup group = new BudgetGroup(id, name);


        // Loop through and add children
        JSONArray childIdJson = json.getJSONArray("Categories");
        for (int i = 0; i < childIdJson.length(); i++) {

            int childId = childIdJson.getInt(i);

            if (allCategories.containsKey(childId)) {

                group.getCategories().add(allCategories.get(childId));
            }
        }


        // Return the group
        return group;
    }

    public PointGain parsePointGain(JSONObject json) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("Title");
        String descrition = json.getString("Description");
        int points = json.getInt("Points");


        // Create Gain from properties
        return new PointGain(id, name, descrition, points);
    }

    public Reward parseReward(JSONObject json) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("Title");
        String description = json.getString("Description");
        int cost = json.getInt("Cost");


        // Create Reward from properties
        return new Reward(id, name, description, cost);
    }

    public RewardTaken parseRewardTaken(JSONObject json, List<Reward> allRewards) throws  JSONException {

        // Get properties
        int id = json.getInt("ID");
        int rewardID = json.getInt("Reward");


        // Fill relation
        for (Reward reward : allRewards) {
            if (reward.getId() == rewardID) {

                return new RewardTaken(id, reward);
            }
        }

        throw new JSONException("Reward Not Found");
    }




    /*
        V3 Parser
     */
    public ATM parseATM(JSONObject json) throws JSONException {

        // Get properties
        int id = json.getInt("ID");
        String name = json.getString("Title");
        double cost = json.getDouble("Cost");
        double lat = json.getDouble("Latitude");
        double lon = json.getDouble("Longitude");


        // Create ATM from properties
        return new ATM(id, name, lat, lon, cost);
    }

    public HeatPoint parseHeatPoint(JSONObject json) throws  JSONException {

        // Get properties
        double lat = json.getDouble("Latitude");
        double lon = json.getDouble("Longitude");
        int radius = json.getInt("Radius");


        // Get HeatPoint from properties
        return new HeatPoint(lat, lon, radius);
    }
}
