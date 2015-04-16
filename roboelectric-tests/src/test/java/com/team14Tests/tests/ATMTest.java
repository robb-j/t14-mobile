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

import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ATMTest {

    //Setup global test variables
    private ATM testATM;

    @Before
    public void setup() {
        //Setup variables here
    }

    //Test the Constructor and get/set methods
    @Test
    public void testATMConstructor() {
        testATM = new ATM(3, "testATM", 100, 200, 300);
        assertEquals(testATM.getId(), 3);
        assertEquals(testATM.getName(), "testATM");
        assertEquals(testATM.getLatitude(), 100.00, 0.01);
        assertEquals(testATM.getLongitude(), 200.00, 0.01);
        assertEquals(testATM.getCost(), 300.00, 0.01);
    }
}
