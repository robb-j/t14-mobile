package com.team14Tests.tests;

/**
 * Template Class created by Jack on 17/02/2015.
 * Test Class created by Aidan on 01/04/2015
 */

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class AccountTest {

    //Setup global test variables
    private Account testAccount1;
    private Account testAccount2;
    private Product testProduct;

    @Before
    public void setup() {
        testProduct = new Product(10, "testProduct", "testContent");
        testAccount1 = new Account(99, "test1", 100, 10, testProduct);
        testAccount2 = new Account(100, "test2", 200, 20);

    }

    //Constructor 1 & get/set method tests
    @Test
    public void testConstructor1SetsId() {
        assertEquals(testAccount1.getId(), 99);
    }

    @Test
    public void testConstructor1SetsName() {
        assertEquals(testAccount1.getName(), "test1");
    }

    @Test
    public void testConstructor1SetsBalance() {
        assertEquals(testAccount1.getBalance(), 100.00, 0.01);
    }

    @Test
    public void testConstructor1SetsOverDraftLimit() {
        assertEquals(testAccount1.getOverdraftLimit(), 10.00, 0.01);
    }

    @Test
    public void testConstructor1SetsProduct() {
        assertEquals(testAccount1.getProduct().getTitle(), "testProduct");
    }


    //Constructor 2 & get/set method tests
    @Test
    public void testConstructor2SetsId() {
        assertEquals(testAccount2.getId(), 100);
    }

    @Test
    public void testConstructor2SetsName() {
        assertEquals(testAccount2.getName(), "test2");
    }

    @Test
    public void testConstructor2SetsBalance() {
        assertEquals(testAccount2.getBalance(), 200.00, 0.01);
    }

    @Test
    public void testConstructor2SetsOverDraftLimit() {
        assertEquals(testAccount2.getOverdraftLimit(), 20.00, 0.01);
    }

    @Test
    public void testConstructor2SetsProduct() {
        assertEquals(testAccount2.getProduct(), null);
    }

    //Test get Transactions
    @Test
    public void testGetTransactions() {
        List<Transaction> emptyList = new ArrayList<Transaction>();
        assertEquals(emptyList, testAccount2.getTransactions());
    }

    //Test describeContents
    @Test
    public void testDescribeContents() {
        assertEquals(testAccount2.describeContents(), 0);
    }

    //Test get/set first transaction
    @Test
    public void testSetFirstTransaction() {
        Date testDate = new Date(1000);
        testAccount1.setFirstTransaction(testDate);
        assertEquals(testAccount1.getFirstTransaction(), testDate);
    }

}
