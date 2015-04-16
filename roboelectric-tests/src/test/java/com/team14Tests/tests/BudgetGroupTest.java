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

import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class BudgetGroupTest {

    //Setup global test variables
    private BudgetGroup testBudgetGroup;
    private BudgetGroup testBudgetGroup2;

    @Before
    public void setup() {
        testBudgetGroup = new BudgetGroup(4, "newBudget");
    }

    //Test BudgetGroup constructor and related get/set methods
    @Test
    public void testBudgetCategoryConstructor() {
        //Constructor used in setup()
        assertEquals(4, testBudgetGroup.getId());
        assertEquals("newBudget", testBudgetGroup.getName());
    }

    //Test alternative Constructor
    @Test
    public void testAlternateBudgetCategoryConstructor() {
        testBudgetGroup2 = new BudgetGroup(testBudgetGroup2);
        assertEquals(0, testBudgetGroup2.getId());
        assertEquals(null, testBudgetGroup2.getName());
    }

    //Test get Categories
    @Test
    public void testGetCategoriesInitiallyReturnsNull() {
        assertEquals(0, testBudgetGroup.getCategories().size());
    }

    //Test getMode() and initial Mode value
    @Test
    public void getMode() {
        assertEquals(testBudgetGroup.getMode(), BudgetGroup.Mode.UNCHANGED);
    }

    //Test setMode()
    @Test
    public void setMode() {
        testBudgetGroup.setMode(BudgetGroup.Mode.EDITED);
        assertEquals(testBudgetGroup.getMode(), BudgetGroup.Mode.EDITED);
    }

    //Test setDeleted()
    @Test
    public void setDeleted() {
        testBudgetGroup.setDeleted();
        assertEquals(testBudgetGroup.getId(), -2);
    }

    //Test getBudget()
    @Test
    public void testGetBudgetReturnsTotalBudget(){
        assertEquals(0, testBudgetGroup.getBudget(), 1);
    }

}
