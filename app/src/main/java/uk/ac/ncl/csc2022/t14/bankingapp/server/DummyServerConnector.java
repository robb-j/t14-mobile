package uk.ac.ncl.csc2022.t14.bankingapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerBudgetingInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;

/**
 * Created by Sam on 17/02/2015.
 */
public class DummyServerConnector implements ServerInterface, ServerBudgetingInterface {


    @Override
    public void login(String username, char[] password, int[] indices, LoginDelegate delegate) {

        //boolean passed = false;

        /* Correct credentials */
        /*
        if (username.equals("Jeff")) {
            if (password[0] == 's' && indices[0] == 2) {
                User user = new User(26, "Bobby99", "Jeff", "Ree", "23/08/1982");
                Product product1 = new Product(1, "Saving Accounts", "This is going to save you money.");
                Product product2 = new Product(1, "ISA", "Use this.");
                List<Product> products = new ArrayList<Product>();
                products.add(product1);
                products.add(product2);
                List<Reward> rewards = new ArrayList<>();
                delegate.loginPassed(products, rewards);
                passed = true;
            }
        }
        if (!passed) {
            delegate.loginFailed("Invalid login details");
        }*/



        if (username.equals("Fail")) {

            delegate.loginFailed("There was a problem logging in");
        }

        else {

            User user = new User(1, "Geoff95", "Geoff", "Butcher", "22/08/2995");


            // Create the products
            Product product1 = new Product(2, "Saving Accounts", "This is going to save you money.");
            Product product2 = new Product(3, "ISA", "Use this.");
            Product product3 = new Product(4, "Student Account", "Great interest, for the first year");
            List<Product> allProducts = new ArrayList<Product>();
            allProducts.add(product1);
            allProducts.add(product2);
            allProducts.add(product3);


            // Create some accounts
            user.getAccounts().add(new Account(5, "Student Account", 523.33, 500, product3));
            user.getAccounts().add(new Account(6, "Bills Account", -120.18, 750, product3));
            user.getAccounts().add(new Account(7, "Savings Account", 1219.01, 0, product1));

            // Create some rewards
            List<Reward> allRewards = new ArrayList<>();
            Reward r1 = new Reward(100, "Amazon Voucher", "£20 to spend on amazon.co.uk", 80);
            Reward r2 = new Reward(101, "Odeon Cinema Tickets", "Tickets to a film of your choice", 30);
            allRewards.add(r1);
            allRewards.add(r2);

            // 'Filter' out the unused products ones
            List<Product> unusedProducts = new ArrayList<Product>();
            unusedProducts.add(product2);


            DataStore.sharedInstance().setCurrentUser(user);
            DataStore.sharedInstance().setProducts(unusedProducts);
            DataStore.sharedInstance().setRewards(allRewards);
            DataStore.sharedInstance().setToken("DummyTokenThatIsReallyLong");

            delegate.loginPassed(user);
        }

    }

    @Override
    public void loadTransactions(Account account, int month, int year, String token, TransactionDelegate delegate) {

        final int fMonth = month;
        final int fYear = year;

        // Loading progress
        class TransactionTimerTask extends TimerTask {
            private String token;
            private Account account;
            private TransactionDelegate delegate;

            @Override
            public void run() {
                if (token.equals("DummyTokenThatIsReallyLong")) {

                    /* February test data --- will be deleted when pulling data from the server */
                    List<Transaction> transactions = new ArrayList<Transaction>();
                    Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance(), cal3 = Calendar.getInstance();
                    cal.set(Calendar.YEAR, 2015);
                    cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 22);
                    cal2.set(Calendar.YEAR, 2014);
                    cal2.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal2.set(Calendar.DAY_OF_MONTH, 17);
                    cal3.set(Calendar.YEAR, 2015);
                    cal3.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal3.set(Calendar.DAY_OF_MONTH, 12);
                    transactions.add(new Transaction(20, 120.05, cal.getTime(), account, "payee1"));
                    transactions.add(new Transaction(21, -1.21, cal.getTime(), account, "payee3"));
                    transactions.add(new Transaction(22, -55.21, cal.getTime(), account, "payee6"));
                    transactions.add(new Transaction(23, -6.31, cal2.getTime(), account, "payee2"));
                    transactions.add(new Transaction(24, -29.31, cal2.getTime(), account, "payee7"));
                    transactions.add(new Transaction(25, -33.11, cal3.getTime(), account, "payee4"));
                    transactions.add(new Transaction(26, -5.99, cal3.getTime(), account, "payee5"));
                    transactions.add(new Transaction(27, 21.34, cal3.getTime(), account, "payee8"));
                    transactions.add(new Transaction(28, -23.73, cal3.getTime(), account, "payee9"));
                    transactions.add(new Transaction(29, -2.43, cal3.getTime(), account, "payee10"));
                    transactions.add(new Transaction(30, -7.49, cal3.getTime(), account, "payee11"));
                    transactions.add(new Transaction(31, -35.91, cal3.getTime(), account, "payee12"));
                    transactions.add(new Transaction(32, -50.99, cal3.getTime(), account, "payee13"));
                    transactions.add(new Transaction(33, 74.13, cal3.getTime(), account, "payee14"));
                    transactions.add(new Transaction(34, -3.42, cal3.getTime(), account, "payee15"));

                    /* March test data --- will be deleted when pulling data from the server */
                    List<Transaction> transactionsMar = new ArrayList<Transaction>();
                    Calendar cal4 = Calendar.getInstance(), cal5 = Calendar.getInstance(), cal6 = Calendar.getInstance();
                    cal4.set(Calendar.YEAR, 2015);
                    cal4.set(Calendar.MONTH, Calendar.MARCH);
                    cal4.set(Calendar.DAY_OF_MONTH, 22);
                    cal5.set(Calendar.YEAR, 2015);
                    cal5.set(Calendar.MONTH, Calendar.MARCH);
                    cal5.set(Calendar.DAY_OF_MONTH, 17);
                    cal6.set(Calendar.YEAR, 2015);
                    cal6.set(Calendar.MONTH, Calendar.MARCH);
                    cal6.set(Calendar.DAY_OF_MONTH, 12);
                    transactionsMar.add(new Transaction(35, 25.20, cal4.getTime(), account, "payee1"));
                    transactionsMar.add(new Transaction(36, -10.13, cal4.getTime(), account, "payee3"));
                    transactionsMar.add(new Transaction(37, -3.14, cal5.getTime(), account, "payee6"));
                    transactionsMar.add(new Transaction(38, -4.98, cal5.getTime(), account, "payee2"));
                    transactionsMar.add(new Transaction(39, -8.99, cal5.getTime(), account, "payee7"));
                    transactionsMar.add(new Transaction(40, -2.99, cal6.getTime(), account, "payee4"));
                    transactionsMar.add(new Transaction(41, -1.99, cal6.getTime(), account, "payee5"));
                    transactionsMar.add(new Transaction(42, -0.99, cal6.getTime(), account, "payee8"));
                    transactionsMar.add(new Transaction(43, 66.21, cal6.getTime(), account, "payee9"));
                    transactionsMar.add(new Transaction(44, -33.1, cal6.getTime(), account, "payee10"));

                    Calendar monthCal = Calendar.getInstance();
                    monthCal.set(fYear, fMonth, 1);
                    Date date = monthCal.getTime();
                    if (fMonth == Calendar.FEBRUARY && fYear == 2015) {
                        delegate.transactionsLoaded(account, transactions);
                    } else if (fMonth == Calendar.MARCH && fYear == 2015) {
                        delegate.transactionsLoaded(account, transactionsMar);
                    } else {
                        delegate.transactionsLoaded(account, null);
                    }

                } else {
                    delegate.transactionsLoadFailed("Authentication error.");
                }
            }
        }

        Timer timer = new Timer();
        TransactionTimerTask task = new TransactionTimerTask();
        task.account = account;
        task.delegate = delegate;
        task.token = token;
        timer.schedule(task, 1000);


    }

    @Override
    public void makeTransfer(int accFromId, int accToId, double amount, String token, TransferDelegate delegate) {

        if (token.equals("DummyTokenThatIsReallyLong")) {

            //Get user from the data store
            User user = DataStore.sharedInstance().getCurrentUser();

            // Get accounts to be transferred from and to from the user above
            Account accountFrom = user.getAccountForId(accFromId);
            Account accountTo = user.getAccountForId(accToId);

            // If we have sufficient funds available to transfer
            if (amount <= accountFrom.getBalance()) {

                // Set the balances accordingly
                accountFrom.setBalance(accountFrom.getBalance() - amount);
                accountTo.setBalance(accountTo.getBalance() + amount);

                delegate.transferPassed(accountFrom, accountTo, amount);
            } else {
                delegate.transferFailed("Insufficient funds");
            }
        } else {
            delegate.transferFailed("Authentication error.");
        }

    }

    @Override
    public void loadNewPaymentsForUser(NewPaymentsDelegate delegate) {

        // Get the token and user from the data store
        String token = DataStore.sharedInstance().getToken();
        User user = DataStore.sharedInstance().getCurrentUser();

        // If the token is correct
        if (token.equals("DummyTokenThatIsReallyLong")) {
            // And there is a user to act upon
            if (!(user == null)) {
                // As part of dummy just created some transactions instead of got them from user
                List<Transaction> transactions = new ArrayList<Transaction>();
                Calendar cal = Calendar.getInstance();
                // This corresponds to the Student Account
                Account account = user.getAccountForId(5);

                // Stole these from TransactionTimerTask above
                cal.set(Calendar.YEAR, 2015);
                cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                cal.set(Calendar.DAY_OF_MONTH, 22);
                transactions.add(new Transaction(20, 120.05, cal.getTime(), account, "payee1"));
                transactions.add(new Transaction(21, -1.21, cal.getTime(), account, "payee3"));
                transactions.add(new Transaction(22, -55.21, cal.getTime(), account, "payee6"));

                delegate.newPaymentsLoaded(transactions);
            }
            else {
                delegate.newPaymentsLoadFailed("No user detected");
            }
        }
        else {
            delegate.newPaymentsLoadFailed("Authentication error.");
        }
    }

    // TODO Give each different path its own response so devs can easily see what's happening
    @Override
    public void categorisePayments(List<Categorisation> categorisations, CategoriseDelegate delegate) {

        // Get the token from the data store
        String token = DataStore.sharedInstance().getToken();
        boolean hasNewSpin = false;

        // If the token is correct
        if (token.equals("DummyTokenThatIsReallyLong"))
        {
            // If list is empty fail
            if (categorisations.size() == 0) {
                delegate.categorisationFailed("Cannot categorise empty list");
            }
            // If list is of size 1 pass but don't award a new spin
            if (categorisations.size() == 1) {
                delegate.categorisationPassed(hasNewSpin);
            }
            // If list is at least 2 elements large then pass and award a new spin
            else {
                hasNewSpin = true;
                delegate.categorisationPassed(hasNewSpin);
            }
        }
        else
        {
            delegate.categorisationFailed("Authentication error.");
        }

    }

    @Override
    public void updateBudget(MonthBudget newBudget, BudgetUpdateDelegate delegate) {

        // Get the token from the data store
        String token = DataStore.sharedInstance().getToken();

        // If the token is correct
        if (token.equals("DummyTokenThatIsReallyLong"))
        {
            // Update the user's budget
            DataStore.sharedInstance().getCurrentUser().setCurrentBudget(newBudget);
            // Notify delegate that completion was successful
            delegate.updateBudgetPassed();
        }
        else
        {
            delegate.updateBudgetFailed("Authentication error.");
        }

    }

    @Override
    public void chooseReward(Reward reward, ChooseRewardDelegate delegate) {

        // Get the token from the data store
        String token = DataStore.sharedInstance().getToken();
        List<Reward> rewards = DataStore.sharedInstance().getRewards();
        // If the token is correct
        if (token.equals("DummyTokenThatIsReallyLong"))
        {

            // Take from global store of rewards
            for (Reward r : rewards) {
                // If the reward matches the one we passed to this method
                if (reward.getId() == r.getId()) {
                    // Give user reward
                    delegate.chooseRewardPassed();
                    break;
                }
            }
        }
        else
        {
            delegate.chooseRewardFailed("Authentication error.");
        }

    }

    @Override
    public void performSpin(PointSpinDelegate delegate) {

        // Use for integer RNG
        Random rand = new Random();

        // RNG picks a multiple of 10 from 10 to 100
        int points = (rand.nextInt(10) + 1) * 10;
        // Pass delegate with amount of points
        delegate.spinPassed(points);
    }
}
