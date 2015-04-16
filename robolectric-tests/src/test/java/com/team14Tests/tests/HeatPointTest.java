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

import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class HeatPointTest {

    //Setup global test variables
    private HeatPoint testHeatPoint;

    @Before
    public void setup() {
        testHeatPoint = new HeatPoint(100, 200, 5);
    }

    //Test Setup of HeatPoint and get/set methods
    @Test
    public void testConstructionOfHeatPoint() {
        //Constructor used in setup()
        assertEquals(100, testHeatPoint.getLatitude(), 0.01);
        assertEquals(200, testHeatPoint.getLongitude(), 0.01);
        assertEquals(5, testHeatPoint.getRadius());
    }
}
