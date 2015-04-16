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

import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class BudgetCategoryTest {

    //Setup global test variables
    private BudgetCategory testBudgetCategory;

    @Before
    public void setup() {
        testBudgetCategory = new BudgetCategory(2, "myBudget", 200, 50);
    }

    //Test the Constructor and related get/set methods
    @Test
    public void testBudgetCategoryConstructor() {
        //Constructor used in setup()
        assertEquals(testBudgetCategory.getId(), 2);
        assertEquals(testBudgetCategory.getName(), "myBudget");
        assertEquals(testBudgetCategory.getBudgeted(), 200.00, 0.01);
        assertEquals(testBudgetCategory.getSpent(), 50, 0.01);
    }

    //Test getMode() and initial Mode value
    @Test
    public void getMode() {
        assertEquals(testBudgetCategory.getMode(), BudgetCategory.Mode.UNCHANGED);
    }

    //Test setMode()
    @Test
    public void setMode() {
        testBudgetCategory.setMode(BudgetCategory.Mode.EDITED);
        assertEquals(testBudgetCategory.getMode(), BudgetCategory.Mode.EDITED);
    }

    //Test setDeleted()
    @Test
    public void setDeleted() {
        testBudgetCategory.setDeleted();
        assertEquals(testBudgetCategory.getId(), -2);
    }
}
