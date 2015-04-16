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
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class UserTest {

    //Setup global test variables
    private User testUser;

    @Before
    public void setup() {
        testUser = new User(13, "Bloggsie", "Joe", "Bloggs", "19/9/1999");
    }

    //test the User Constructor and related get/set methods
    @Test
    public void testCanCreateNewUserWithCorrectValues() {
        //Constructor used in setup()
        //Create an empty array for comparison
        List<Account> emptyAccountArray = new ArrayList<Account>();

        //Banking Fields
        assertEquals(13, testUser.getId());
        assertEquals("Bloggsie", testUser.getUsername());
        assertEquals("Joe", testUser.getFirstName());
        assertEquals("Bloggs", testUser.getLastName());
        assertEquals("19/9/1999", testUser.getDob());
        assertEquals("01/01/1970", testUser.getLastFullCategorise());
        assertEquals(emptyAccountArray, testUser.getAccounts());

        //Budgeting Fields
        assertEquals(100, testUser.getPoints());
        assertEquals(5, testUser.getNumberOfSpins());
        assertEquals(0, testUser.getNumNewPayments());
    }

    //Test get/set MonthBudget
    @Test
    public void testCanGetSetMonthBudget(){
        MonthBudget testMonthBudget = new MonthBudget(14);
        testUser.setCurrentBudget(testMonthBudget);
        assertEquals(testMonthBudget.getId(), testUser.getCurrentBudget().getId());
    }

    //Test get/set AllGroups
    @Test
    public void testCanGetSetBudgetGroups(){
        //Setup the list
        BudgetGroup testBudgetGroup = new BudgetGroup(14, "BG");
        List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
        budgetGroupList.add(testBudgetGroup);
        //Tests
        testUser.setAllGroups(budgetGroupList);
        assertEquals(budgetGroupList.get(0).getName(), testUser.getAllGroups().get(0).getName());
    }

    //test getRecentRewards
    @Test
    public void testCanGetListOfRecentRewards(){
        //Create comparison List
        List<RewardTaken> comparisonList = new ArrayList<RewardTaken>();
        //Tests
        assertEquals(comparisonList, testUser.getRecentRewards());
    }

    //test getRecentPoints
    @Test
    public void testCanGetListOfRecentPoints(){
        //Create comparison List
        List<PointGain> comparisonList = new ArrayList<PointGain>();
        //Tests
        assertEquals(comparisonList, testUser.getRecentPoints());
    }

}
