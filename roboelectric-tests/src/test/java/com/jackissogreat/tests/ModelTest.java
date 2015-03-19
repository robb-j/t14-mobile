package com.jackissogreat.tests;

/**
 * Created by Jack on 17/02/2015.
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.*;

import uk.ac.ncl.csc2022.t14.bankingapp.models.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ModelTest {

    User testUser;
    Account testAccount;
    Product testProduct;
    Transaction testTransaction;

    @Before
    public void setup() {

        testUser = new User(1, "Bobby99", "First", "Last", "01/01/1970");
        testProduct = new Product(10, "Product 1", "P1 Desc");
        testAccount = new Account(2,"Student Account", 1000, 500, testProduct);
        testTransaction = new Transaction(3, 300, new Date(), testAccount, "First");
    }

    @Test
    public void canCreateModels() {

        // Test each test object was created
        assertNotNull(testUser);
        assertNotNull(testAccount);
        assertNotNull(testProduct);
        assertNotNull(testTransaction);
    }

    @Test
    public void canConstructAUser() {

        // Test properties were set
        assertEquals(1, testUser.getId());
        assertEquals("Bobby99", testUser.getUsername());
        assertEquals("First", testUser.getFirstName());
        assertEquals("Last", testUser.getLastName());
        assertEquals("01/01/1970", testUser.getDob());
        assertEquals("01/01/1970", testUser.getLastFullCategorise());

        //Need to check if accounts are added properly to a user's account list when this is implemented
    }

    @Test
    public void canConstructAnAccount() {

        // Test properties were set
        assertEquals(2, testAccount.getId());
        assertEquals("Student Account", testAccount.getName());
        assertEquals(1000, testAccount.getBalance(), 0.1);
        assertEquals(500, testAccount.getOverdraftLimit(), 0.1);
        assertEquals(testProduct, testAccount.getProduct());

        // Still need to test transactions list is added to appropriately once it's implemented
    }

    @Test
    public void canConstructAProduct() {

        // Test properties were set
        assertEquals(10, testProduct.getId());
        assertEquals("Product 1", testProduct.getTitle());
        assertEquals("P1 Desc", testProduct.getContent());
    }

    @Test
    public void canConstructATransaction() {

        // Test properties were set
        assertEquals(3, testTransaction.getId());
        assertEquals(300, testTransaction.getAmount(), 0.1);
        assertEquals(testAccount, testTransaction.getAccount());
        assertEquals("First", testTransaction.getPayee());
    }
}
