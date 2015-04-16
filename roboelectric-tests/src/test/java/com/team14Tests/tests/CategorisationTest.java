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
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CategorisationTest {

    //Setup global test variables
    private Categorisation testCategory;
    private Categorisation testCategoryNoLocationInfo;
    private Transaction testTransaction;
    private Account testAccount;
    private BudgetCategory testBudgetCategory;

    @Before
    public void setup() {
        testCategory = new Categorisation(5,100,200);
        testTransaction = new Transaction(25, 0, new Date(1000), testAccount, "name");
        testBudgetCategory = new BudgetCategory(26, "testBudget", 100, 200);
    }

    //Test setup of Categorisation with location information & related get/set methods
    @Test
    public void testCategorisationCreatedWithLocationInformation(){
        //Constructor used in setup()
        assertEquals(5, testCategory.getId());
        assertEquals(100, testCategory.getLatitude(), 0.01);
        assertEquals(200, testCategory.getLongitude(), 0.01);
    }

    //Test setup of Categorisation with no location information
    @Test
    public void testCategorisationCreatedWithNoLocationInformation(){
        testCategoryNoLocationInfo = new Categorisation(6);
        assertEquals(6, testCategoryNoLocationInfo.getId());
        assertEquals(0.0, testCategoryNoLocationInfo.getLatitude(), 0.01);
        assertEquals(0.0, testCategoryNoLocationInfo.getLongitude(), 0.01);
    }

    //test get/set Transaction methods
    @Test
    public void testNoInitialTransaction(){
        assertNull(testCategory.getTransaction());
    }

    @Test
    public void testTransactionCanBeSet(){
        testCategory.setTransaction(testTransaction);
        assertEquals(testTransaction.getAmount(), testCategory.getTransaction().getAmount(), 0.01);
    }

    //test get/set BudgetCategory methods
    @Test
    public void testNoInitialBudgetCategory(){
        assertNull(testCategory.getBudgetCategory());
    }

    @Test
    public void testBudgetCategoryCanBeSet(){
        testCategory.setBudgetCategory(testBudgetCategory);
        assertEquals(testBudgetCategory.getName(), testCategory.getBudgetCategory().getName());
    }

}
