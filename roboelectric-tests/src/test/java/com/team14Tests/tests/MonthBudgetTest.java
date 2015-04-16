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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class MonthBudgetTest {

    //Setup global test variables
    private MonthBudget testMonthBudget;
    private Map<Integer, String> testMapTemplate;
    private List<BudgetGroup> testListTemplate;
    private List<BudgetGroup> testList;

    @Before
    public void setup() {
        testMonthBudget = new MonthBudget(6);
        testList = new ArrayList<>();
        testMapTemplate = new HashMap<>();
        testList.add(new BudgetGroup(2,"two"));
        testList.add(new BudgetGroup(3, "three"));
    }

    //Test the MonthBudget Constructor and related get/set methods
    @Test
    public void testConstructNewMonthBudget(){
        //Constructor used in setup()
        assertEquals(6, testMonthBudget.getId());
    }

    //Test get method for budgetAmount
    @Test
    public void testGetBudgetAmountReturnsBudgetAmountMap(){
        assertEquals(testMapTemplate, testMonthBudget.getBudgetAmount());
    }

    //Test get method for categoryNameChanges
    @Test
    public void testGetCategoryChangesMapReturnsCategoryNameChangesMap(){
        assertEquals(testMapTemplate, testMonthBudget.getBudgetAmount());
    }

    //Test get method for budgetAmount
    @Test
    public void testGetGroupNameChangesReturnsGroupNameChangesMap(){
        assertEquals(testMapTemplate, testMonthBudget.getBudgetAmount());
    }

    //Test setNewGroups() & getNewGroups()
    @Test
    public void testCanSetAndReturnNewGroups(){
        testMonthBudget.setNewGroups(testList);
        assertEquals(testList.get(1).getName(), testMonthBudget.getNewGroups().get(1).getName());
    }


}
