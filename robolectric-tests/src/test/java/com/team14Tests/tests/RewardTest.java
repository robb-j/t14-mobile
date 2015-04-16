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

import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class RewardTest {

    //Setup global test variables
    private Reward testReward;

    @Before
    public void setup() {
        testReward = new Reward(9, "testRew", "testDesc", 50);
    }

    //Test the Reward Constructor and related get/set methods
    @Test
    public void testCanCreateNewRewardWithCorrectValues() {
        //Constructor used in setup()
        assertEquals(9, testReward.getId());
        assertEquals("testRew", testReward.getName());
        assertEquals("testDesc", testReward.getDescription());
        assertEquals(50, testReward.getCost());
    }
}
