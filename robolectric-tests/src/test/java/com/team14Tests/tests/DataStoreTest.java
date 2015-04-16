package com.team14Tests.tests;

/**
 * Created by Jack on 17/02/2015.
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class DataStoreTest {

    User testUser;
    List<Product> testProducts;
    List<Reward> testRewards;
    String token;
    @Before
    public void setup() {

        // Create a user for the data store to hold
        testUser = new User(10, "Bobby99", "First", "Last", "01/01/1970");

        // Create a list of products for the data store to hold
        Product testProduct1 = new Product(1, "pName1", "pDesc1");
        Product testProduct2 = new Product(2, "pName2", "pDesc2");
        testProducts = new ArrayList<>();
        testProducts.add(testProduct1);
        testProducts.add(testProduct2);

        // Create a list of rewards for the data store to hold
        Reward testReward1 = new Reward(3, "rName1", "rDesc1", 10);
        Reward testReward2 = new Reward(4, "rName2", "rDesc2", 20);
        testRewards = new ArrayList<>();
        testRewards.add(testReward1);
        testRewards.add(testReward2);

        // Create a token for the data store to hold
        token = "correctToken";

        // Make the objects above the ones which the data store is holding
        DataStore.sharedInstance().setCurrentUser(testUser);
        DataStore.sharedInstance().setProducts(testProducts);
        DataStore.sharedInstance().setRewards(testRewards);
        DataStore.sharedInstance().setToken(token);
    }

    @Test
    public void canStoreUser() {

        User u = DataStore.sharedInstance().getCurrentUser();
        assertNotNull(u);
    }

    @Test
    public void canStoreProducts() {

        List<Product> products = DataStore.sharedInstance().getProducts();
        assertNotNull(products.get(0));
        assertNotNull(products.get(1));
    }

    @Test
    public void canStoreRewards() {

        List<Reward> rewards = DataStore.sharedInstance().getRewards();
        assertNotNull(rewards.get(0));
        assertNotNull(rewards.get(1));
    }

    @Test
    public void canStoreToken() {
        assertEquals(token, DataStore.sharedInstance().getToken());
    }


}
