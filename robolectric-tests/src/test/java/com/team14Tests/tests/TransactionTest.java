package com.team14Tests.tests;

/**
 * Template Class created by Jack on 17/02/2015.
 * Test Class created by Aidan on 01/04/2015
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TransactionTest {

    //Setup global test variables
    private Account testAccount;
    private Transaction testTransaction;
    private Product testProduct;

    @Before
    public void setup() {
        testAccount = new Account(99, "test1", 100, 10, testProduct);
        testTransaction = new Transaction(12, 25, new Date(1000), testAccount, "from");
    }

    //Test the Transaction Constructor and related get/set methods
    @Test
    public void testCanCreateNewTransactionWithCorrectValues() {
        //Constructor used in setup()
        assertEquals(12, testTransaction.getId());
        assertEquals(25, testTransaction.getAmount(), 0.01);
        assertEquals(new Date(1000), testTransaction.getDate());
        assertEquals(testAccount.getName(), testTransaction.getAccount().getName());
        assertEquals("from", testTransaction.getPayee());
    }

    //Test get/set Categorised
    @Test
    public void testCanGetSetCategorised(){
        testTransaction.setCategorised(true);
        assertTrue(testTransaction.getCategorised());
    }

    //Test get/set Category
    @Test
    public void testCanGetSetCategory(){
        testTransaction.setCategory("testCategory");
        assertEquals("testCategory", testTransaction.getCategory());
    }

}
