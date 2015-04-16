package com.team14Tests.tests;

/**
 * Tests the response of LiveConnector when giving it local JSON
 * Created by Rob A on 05/04/15.
 */
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ATMDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.HeatMapDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LogoutDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONFetcher;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class LiveConnectorTest {

    private LiveServerConnector testConnector;

    @Before
    public void setup() {
        //Setup variables here
        testConnector = new LiveServerConnector();
        testConnector.setBaseUrl(JSONFetcher.TEST_MODE_BASEURL);

        DataStore.sharedInstance().setToken("DummyToken");

        User user = new User(1, "rob", "Rob", "Anderson", "22");
        DataStore.sharedInstance().setCurrentUser(user);
    }

    @After
    public void tearDown() {

        DataStore.sharedInstance().setCurrentUser(null);
    }

    @Test
    public void testSetup() {

        assertNotNull(testConnector);
    }



    /*
        V1 Response & Parse Tests
    */
    @Test
    public void testLiveLoginResponse() {

        // Create test data
        String username = "geoff";
        char[] password = {'p', 'a', 's'};
        int[] indecies = {0, 1, 2};


        // Create delegate to get response
        LoginDelegate delegate = new LoginDelegate() {
            @Override
            public void loginPassed(User user) {

                assertNotNull(user);
            }

            @Override
            public void loginFailed(String errMessage) {

                fail(errMessage);
            }
        };

        testConnector.login(username, password, indecies, delegate);

        // Test the token
        String token = DataStore.sharedInstance().getToken();
        assertEquals(token, "Ppx9BeXdbotVtfxQJ5H3v0bEl9kFtciZ1pLcCgRP2GbGvPVX4LgVFKEB98IgflkZ");


        // Test the user
        User user = DataStore.sharedInstance().getCurrentUser();
        assertNotNull(user);


        List<Product> products = DataStore.sharedInstance().getProducts();
        List<Product> newProds = DataStore.sharedInstance().getNewProducts();
        List<Reward> rewards = DataStore.sharedInstance().getRewards();


        // Test Products
        assertEquals(products.size(), 3);
        assertEquals(1, products.get(0).getId());
        assertEquals("Savings Account", products.get(0).getTitle());
        assertNotNull(products.get(0).getContent());


        // Test New Products
        assertEquals(1, newProds.size());
        assertEquals(3, newProds.get(0).getId());


        // Test Rewards
        assertEquals(rewards.size(), 2);
        assertEquals(rewards.get(0).getId(), 1);
        assertEquals(rewards.get(0).getName(), "Cinema Tickets");
        assertEquals(rewards.get(0).getCost(), 300);
        assertNotNull(rewards.get(0).getDescription());


        // Test the User's properties
        assertEquals(2, user.getId());
        assertEquals("geoff", user.getUsername());
        assertEquals("Geoff", user.getFirstName());
        assertEquals("Butcher", user.getLastName());
        assertEquals(3, user.getNumberOfSpins());
        assertEquals(450, user.getPoints());
        assertEquals(2, user.getNumNewPayments());

        assertEquals(2, user.getAccounts().size());
        assertEquals(2, user.getAllGroups().size());
        assertEquals(2, user.getRecentPoints().size());
        assertEquals(1, user.getRecentRewards().size());


        // Test An Account
        Account account = user.getAccounts().get(0);
        assertEquals(7, account.getId());
        assertEquals("Student Account", account.getName());
        assertEquals(131.97, account.getBalance(), 0.0001);
        assertNotNull(account.getFirstTransaction());
        assertNotNull(account.getProduct());
        assertEquals(2, account.getProduct().getId());


        // Test A Group
        BudgetGroup testGroup = user.getAllGroups().get(0);
        assertNotNull(testGroup);
        assertEquals(3, testGroup.getId());
        assertEquals("Bills", testGroup.getName());
        assertEquals(2, testGroup.getCategories().size());

        // Test A Category
        BudgetCategory testCat = testGroup.getCategories().get(0);
        assertEquals(7, testCat.getId());
        assertEquals("Rent", testCat.getName());
        assertEquals(500.0, testCat.getBudgeted(), 0.1);
        assertEquals(450.0, testCat.getSpent(), 0.1);

        // Test a PointGain
        PointGain testGain = user.getRecentPoints().get(0);
        assertEquals(3, testGain.getId());
        assertEquals("Spin Reward", testGain.getName());
        assertEquals(25, testGain.getPoints());
        assertNotNull(testGain.getDescription());

        // Test a RewardTaken
        RewardTaken testTaken = user.getRecentRewards().get(0);
        assertEquals(2, testTaken.getId());
        assertNotNull(testTaken.getReward());
    }

    @Test
    public void testLiveLoadTransactionsResponse() {

        // Create test data
        Account account = new Account(7, "Test Account", 100, 500);


        // Create delegate to test response
        TransactionDelegate delegate = new TransactionDelegate() {
            @Override
            public void transactionsLoaded(Account account, List<Transaction> transactions) {

                assertNotNull(account);
                assertNotNull(transactions);

                // Test a transaction
                assertEquals(1, transactions.size());
                assertEquals("Spotify", transactions.get(0).getPayee());
                assertEquals(-9.99, transactions.get(0).getAmount(), 0.1);
                //assertEquals(1427846400, transactions.get(0).getDate().getTime()/1000);
                // TODO: Date is a day off? :-/
            }

            @Override
            public void transactionsLoadFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.loadTransactions(account, 1, 1, delegate);



        // Test the balance was updated
        assertEquals(131.97, account.getBalance(), 0.1);

    }

    @Test
    public void testLiveTransferResponse() {

        // Setup test data
        Account a = new Account(7, "Student", 100, 500.0);
        Account b = new Account(8, "Saver", 200, 0.0);


        // Create a delegate to get response
        TransferDelegate delegate = new TransferDelegate() {
            @Override
            public void transferPassed(Account accFrom, Account accTo, double amount) {

                assertNotNull(accFrom);
                assertNotNull(accTo);

                assertEquals(121.97, accFrom.getBalance(), 0.1);
                assertEquals(1031.91, accTo.getBalance(), 0.1);
            }

            @Override
            public void transferFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.makeTransfer(a, b, 100, delegate);
    }

    @Test
    public void testLogoutResponse() {

        // Create a delegate to test the response
        LogoutDelegate delegate = new LogoutDelegate() {
            @Override
            public void logoutPassed() {

            }

            @Override
            public void logoutFailed(String message) {

                fail(message);
            }
        };


        // Call the method
        testConnector.logout(delegate);


        // Make sure the data was unset
        assertNull(DataStore.sharedInstance().getCurrentUser());
        assertNull(DataStore.sharedInstance().getToken());
        assertEquals(0, DataStore.sharedInstance().getProducts().size());
        assertEquals(0, DataStore.sharedInstance().getRewards().size());
    }




    /*
            V2 Response & Parse Tests
     */

    @Test
    public void testNewPaymentsResponse() {

        // Setup test data
        Account a = new Account(7, "Account", 100, 200);
        DataStore.sharedInstance().getCurrentUser().getAccounts().add(a);


        // Create a delegate to test response
        NewPaymentsDelegate delegate = new NewPaymentsDelegate() {
            @Override
            public void newPaymentsLoaded(List<Transaction> transactions) {

                assertNotNull(transactions);
                assertEquals(3, transactions.size());

                Transaction tran = transactions.get(0);
                assertEquals(8, tran.getId());
            }

            @Override
            public void newPaymentsLoadFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.loadNewPaymentsForUser(delegate);
    }

    @Test
    public void testCategoriseResponse() {

        // Create test params
        List<Categorisation> categorisations = new ArrayList<>();

        final BudgetCategory cat = new BudgetCategory(9, "Category", 100, 120);
        Transaction tran = new Transaction(8, 100, null, null, "Spotify");

        BudgetGroup group = new BudgetGroup(5, "Group");
        group.getCategories().add(cat);

        DataStore.sharedInstance().getCurrentUser().getAllGroups().add(group);

        Categorisation catorg = new Categorisation(0);
        catorg.setBudgetCategory(cat);
        catorg.setTransaction(tran);


        // Create delegate to test response
        CategoriseDelegate delegate = new CategoriseDelegate() {
            @Override
            public void categorisationPassed(boolean hasNewSpin) {

                assertEquals(110.03, cat.getSpent(), 0.1);
                assertTrue(hasNewSpin);
                assertEquals(15, DataStore.sharedInstance().getCurrentUser().getNumberOfSpins());
            }

            @Override
            public void categorisationFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.categorisePayments(categorisations, delegate);
    }

    @Test
    public void testUpdateBudgetResponse() {

        // Create test params
        List<BudgetGroup> groups = new ArrayList<>();


        // Create delegate to test response
        BudgetUpdateDelegate delegate = new BudgetUpdateDelegate() {
            @Override
            public void updateBudgetPassed() {

                // Check budgets have changed
            }

            @Override
            public void updateBudgetFailed(String errMessage) {

            }
        };


        // Call the method
        testConnector.updateBudget(groups, delegate);


        // Test the groups were added to the user
        User user = DataStore.sharedInstance().getCurrentUser();
        assertEquals(2, user.getAllGroups().size());



        // Get the first Group & Category to test
        BudgetGroup group1 = user.getAllGroups().get(0);
        BudgetCategory category = group1.getCategories().get(0);


        // Test the right number of categorise were parsed
        assertEquals(1, group1.getCategories().size());
        assertEquals(2, user.getAllGroups().get(1).getCategories().size());


        // Test the Group's id & title were set
        assertEquals(3, group1.getId());
        assertEquals("Bills", group1.getName());


        // Test the Category's id, name, budgeted & balance were parsed
        assertEquals(7, category.getId());
        assertEquals("Rent", category.getName());
        assertEquals(500.00, category.getBudgeted(), 0.0001);
        assertEquals(450.00, category.getSpent(), 0.0001);
    }

    @Test
    public void testChooseRewardResponse() {

        // Create test params
        Reward reward = new Reward(2, "Title", "Content", 200);

        List<Reward> allRewards = new ArrayList<>();
        allRewards.add(reward);
        DataStore.sharedInstance().setRewards(allRewards);


        // Create delegate to test response
        ChooseRewardDelegate delegate = new ChooseRewardDelegate() {
            @Override
            public void chooseRewardPassed() {

                assertEquals(1, DataStore.sharedInstance().getCurrentUser().getRecentRewards().size());
            }

            @Override
            public void chooseRewardFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call method to test
        testConnector.chooseReward(reward, delegate);


        // Test the RewardTaken
        RewardTaken taken = DataStore.sharedInstance().getCurrentUser().getRecentRewards().get(0);
        assertEquals(3, taken.getId());
        assertEquals(2, taken.getReward().getId());
    }

    @Test
    public void testPerformSpinResponse() {

        // Create a delegate to test response
        PointSpinDelegate delegate = new PointSpinDelegate() {
            @Override
            public void spinPassed(int numPoints) {

                assertEquals(20, numPoints);
            }

            @Override
            public void spinFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.performSpin(delegate);


        // Test the user's points was updated
        User user = DataStore.sharedInstance().getCurrentUser();
        assertEquals(170, user.getPoints());
        assertEquals(18, user.getNumberOfSpins());
    }



    /*
        V3 Response & Parse Tests
    */
    @Test
    public void testLoadATMsResponse() {

        // Create a delegate to test the response
        ATMDelegate delegate = new ATMDelegate() {
            @Override
            public void loadATMsPassed(List<ATM> allATMs) {

                assertEquals(2, allATMs.size());
                ATM atm = allATMs.get(0);

                assertEquals(1, atm.getId());
                assertEquals("Citi Bank", atm.getName());
                assertEquals(3.50, atm.getCost(), 0.1);
                assertEquals(53.38697, atm.getLatitude(), 0.000001);
                assertEquals(-1.4776, atm.getLongitude(), 0.000001);
            }

            @Override
            public void loadATMsFailed(String errMessage) {

                fail(errMessage);
            }
        };


        // Call the method
        testConnector.loadATMS(delegate);
    }

    @Test
    public void testLoadHeatMapResponse() {

        // Create test params
        Account account = new Account(7, "Account", 50, 100);
        int[] accounts = {7};

        Date start = new Date(1417305600);
        Date end = new Date(1428364800);


        // create delegate to test response
        HeatMapDelegate delegate = new HeatMapDelegate() {
            @Override
            public void loadHeatMapPassed(List<HeatPoint> allHeatPoints) {

                // Test the response
                assertNotNull(allHeatPoints);
                assertEquals(4, allHeatPoints.size());


                // Test a heat point
                HeatPoint point = allHeatPoints.get(0);
                assertEquals(53.438055, point.getLatitude(), 0.0000001);
                assertEquals(-1.357008, point.getLongitude(), 0.0000001);
                assertEquals(20, point.getRadius());
            }

            @Override
            public void loadHeatMapFailed(String errMessage) {

                fail();
            }
        };


        // Call the methods
        testConnector.loadHeatMap(accounts, start, end, delegate);
    }
}
