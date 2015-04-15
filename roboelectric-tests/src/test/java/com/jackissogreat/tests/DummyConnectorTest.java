package com.jackissogreat.tests;

/**
 * Tests the responses of the dummy server connector
 * Created by Jack on 15/04/15.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

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
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
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
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.json.JSONFetcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class DummyConnectorTest {

    private DummyServerConnector testConnector;

    @Before
    public void setup() {
        //Create the dummy connector object to test with
        testConnector = new DummyServerConnector();

        // Create and set the user to use throughout the testing process
        User user = new User(1, "JCooper", "Jack", "Cooper", "");

        // Give the user's fields values to test

        // Create the products
        Product product1 = new Product(2, "Test Product A", "ID = 2");
        Product product2 = new Product(3, "Test Product B", "ID = 3");
        Product product3 = new Product(4, "Test Product C", "ID = 4");
        List<Product> testProducts = new ArrayList<>();
        testProducts.add(product1);
        testProducts.add(product2);
        testProducts.add(product3);

        DataStore.sharedInstance().setProducts(testProducts);

        // Create some accounts
        user.getAccounts().add(new Account(5, "Test Account A", 523.33, 500, product3));
        user.getAccounts().add(new Account(6, "Test Account B", -120.18, 750, product3));
        user.getAccounts().add(new Account(7, "Test Account C", 1219.01, 0, product1));

        // Create some rewards
        Reward r1 = new Reward(100, "Test Reward 1", "ID = 100; Cost = 80", 80);
        Reward r2 = new Reward(101, "Test Reward 2", "ID = 101; Cost = 30", 30);
        List<Reward> testRewards = new ArrayList<>();
        testRewards.add(r1);
        testRewards.add(r2);

        // Set data store values
        DataStore.sharedInstance().setRewards(testRewards);
        DataStore.sharedInstance().setCurrentUser(user);
        DataStore.sharedInstance().setServerConnector(testConnector);
    }

    @After
    public void tearDown() {
        //After testing has finished reset the user field to null
        DataStore.sharedInstance().setCurrentUser(null);
    }

    @Test
    public void testSetup() {
        //Make sure there is a connector object of some kind
        assertNotNull(testConnector);
    }

    /*
        Testing V1 Connector (Basic banking elements)
    */
    @Test
    public void testLoginResponse() {

        // Create test data
        String username = "geoff";
        char[] password = {'p', 'a', 's'};
        int[] indices = {0, 1, 2};


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

        // Attempt a login
        testConnector.login(username, password, indices, delegate);

        // Test the token
        String token = DataStore.sharedInstance().getToken();
        assertEquals(token, "DummyTokenThatIsReallyLong");

        // Test the user
        User user = DataStore.sharedInstance().getCurrentUser();
        assertNotNull(user);


        // Get the products and rewards from the data store
        List<Product> products = DataStore.sharedInstance().getProducts();
        List<Reward> rewards = DataStore.sharedInstance().getRewards();


        // Test Products
        assertEquals(products.size(), 1);
        assertEquals(3, products.get(0).getId());
        assertEquals("ISA", products.get(0).getTitle());
        assertNotNull(products.get(0).getContent());


        // Test Rewards
        assertEquals(rewards.size(), 12);
        assertEquals(rewards.get(0).getId(), 100);
        assertEquals(rewards.get(0).getName(), "Amazon Voucher");
        assertEquals(rewards.get(0).getCost(), 80);
        assertNotNull(rewards.get(0).getDescription());


        // Test the User's basic properties
        assertEquals(1, user.getId());
        assertEquals("Geoff95", user.getUsername());
        assertEquals("Geoff", user.getFirstName());
        assertEquals("Butcher", user.getLastName());
        assertEquals(100, user.getPoints());

        // Test a user's properties can be set
        DataStore.sharedInstance().getCurrentUser().setNumberOfSpins(3);
        assertEquals(3, user.getNumberOfSpins());
        DataStore.sharedInstance().getCurrentUser().setPoints(350);
        assertEquals(350, user.getPoints());


        // Test An Account
        Account account = user.getAccounts().get(0);
        assertEquals(5, account.getId());
        assertEquals("Student Account", account.getName());
        assertEquals(523.33, account.getBalance(), 0.001);
        assertEquals(500, account.getOverdraftLimit(), 0.001);
        assertNotNull(account.getProduct());
        assertEquals(4, account.getProduct().getId());

    }

    @Test
    public void testDummyTransferResponse() {

        // Setup test data
        Account a = new Account(7, "Student", 100, 500.0);
        Account b = new Account(8, "Saver", 200, 0.0);


        // Create a delegate to get response
        TransferDelegate delegate = new TransferDelegate() {
            @Override
            public void transferPassed(Account accFrom, Account accTo, double amount) {

                assertNotNull(accFrom);
                assertNotNull(accTo);

                assertEquals(0, accFrom.getBalance(), 0.001);
                assertEquals(300, accTo.getBalance(), 0.001);
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
       Testing V2 Connector ( 'Gamification'/Budgeting elements)
     */

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
        DataStore.sharedInstance().setToken("DummyTokenThatIsReallyLong");
        DataStore.sharedInstance().getCurrentUser().setPoints(500);
        testConnector.chooseReward(reward, delegate);


        // Test the RewardTaken
        RewardTaken taken = DataStore.sharedInstance().getCurrentUser().getRecentRewards().get(0);
        assertEquals(100, taken.getId());
        assertEquals(2, taken.getReward().getId());
    }

    @Test
    public void testPerformSpinResponse() {

        // Create a delegate to test response
        PointSpinDelegate delegate = new PointSpinDelegate() {
            @Override
            public void spinPassed(int numPoints) {

                assertEquals(0, numPoints%10);
                assertTrue( numPoints >= 10 && numPoints <= 100);
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
        assertEquals(4, user.getNumberOfSpins());
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

                assertEquals(3, allATMs.size());
                ATM atm = allATMs.get(0);

                assertEquals(0, atm.getId());
                assertEquals("Sainsbury's", atm.getName());
                assertEquals(0, atm.getCost(), 0.1);
                assertEquals(51.141395, atm.getLatitude(), 0.000001);
                assertEquals(0.260385, atm.getLongitude(), 0.000001);
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
                assertEquals(11, allHeatPoints.size());


                // Test a heat point
                HeatPoint point = allHeatPoints.get(0);
                assertEquals(51.141395, point.getLatitude(), 0.0000001);
                assertEquals(0.260385, point.getLongitude(), 0.0000001);
                assertEquals(10, point.getRadius());
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
