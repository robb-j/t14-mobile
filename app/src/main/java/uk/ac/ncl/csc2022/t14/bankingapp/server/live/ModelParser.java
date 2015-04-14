package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        V1 Parser
     */
    public User parseUser(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);

        // Get properties
        int id = parser.getInt("ID");
        String username = parser.getString("Username", "Username");
        String first = parser.getString("FirstName", "First Name");
        String last = parser.getString("LastName", "Last Name");
        String dob = parser.getString("DOB", "DOB");


        // Create the user
        User user = new User(id, username, first, last, dob);


        // Add extra properties
        user.setNumberOfSpins(parser.getInt("NumberOfSpins"));
        user.setPoints(parser.getInt("Points"));

        return user;
    }

    public Account parseAccount(JSONObject json, Map<Integer, Product> allProducts) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("AccountType", "An Account");
        double balance = parser.getDouble("Balance");
        double overdraft = parser.getDouble("OverdraftLimit");

        // Create Account from properties
        Account account = new Account(id , name, balance, overdraft);

        account.setProduct(parser.fillRelation("Product", allProducts));
        account.setFirstTransaction(parser.getDate("FirstTransaction"));

        return account;
    }

    public Transaction parseTransaction(JSONObject json, Account account) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);

        // Get properties
        int id = parser.getInt("ID");
        double amount = parser.getDouble("Amount");
        Date date = parser.getDate("Date");
        String payee = parser.getString("Payee", "No Payee Set");


        // Create Transaction from properties
        return new Transaction(id, amount, date, account, payee);
    }

    public Transaction parseTransaction(JSONObject json, List<Account> allAccounts)  {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);

        return parseTransaction(json, parser.fillRelation("Account", allAccounts));
    }

    public Product parseProduct(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);

        // Get properties
        int id = parser.getInt("ID");
        String title = parser.getString("Title", "A Product");
        String content = parser.getString("Content", "<p> Content Not Found </p>");


        // Create Product from properties
        return new Product(id, title, content);
    }




    /*
        V2 Parser
     */
    public BudgetCategory parseCategory(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("Title", "No Name Set");
        double budgeted = parser.getDouble("Budgeted");
        double spent = parser.getDouble("Balance");


        // Create Category from properties
        return new BudgetCategory(id, name, budgeted, spent);
    }

    public BudgetGroup parseGroup(JSONObject json, Map<Integer, BudgetCategory> allCategories) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("Title", "No Name Set");


        // Create Group from properties
        BudgetGroup group = new BudgetGroup(id, name);


        // Fill the category relation
        List<BudgetCategory> categories = parser.fillRelationToMany("Categories", allCategories);
        group.getCategories().addAll(categories);


        // Return the group
        return group;
    }

    public PointGain parsePointGain(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("Title", "No Name Given");
        String description = parser.getString("Description", "No Description Given");
        int points = parser.getInt("Points");


        // Create Gain from properties
        return new PointGain(id, name, description, points);
    }

    public Reward parseReward(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("Title", "No Name Given");
        String description = parser.getString("Description", "No Description Given");
        int cost = parser.getInt("Cost");


        // Create Reward from properties
        return new Reward(id, name, description, cost);
    }

    public RewardTaken parseRewardTaken(JSONObject json, List<Reward> allRewards) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        Reward reward = parser.fillRelation("Reward", allRewards);

        return new RewardTaken(id, reward);
    }




    /*
        V3 Parser
     */
    public ATM parseATM(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        int id = parser.getInt("ID");
        String name = parser.getString("Title", "No Name Given");
        double cost = parser.getDouble("Cost");
        double lat = parser.getDouble("Latitude");
        double lon = parser.getDouble("Longitude");


        // Create ATM from properties
        return new ATM(id, name, lat, lon, cost);
    }

    public HeatPoint parseHeatPoint(JSONObject json) {

        // Use our own parser to avoid try-catches
        JSONParser parser = new JSONParser(json);


        // Get properties
        double lat = parser.getDouble("Latitude");
        double lon = parser.getDouble("Longitude");
        int radius = parser.getInt("Radius");


        // Get HeatPoint from properties
        return new HeatPoint(lat, lon, radius);
    }
}
