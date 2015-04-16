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

import java.awt.Point;

import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class PointGainTest {

    //Setup global test variables
    private PointGain testPointGain;

    @Before
    public void setup() {
        testPointGain = new PointGain(7, "testPG", "PGDesc", 60);
    }

    //Test PointGainConstructor and related get/set methods
    @Test
    public void testCanConstructNewPointGainWithCorrectInformation() {
        //Constructor used in setup()
        assertEquals(7, testPointGain.getId());
        assertEquals("testPG", testPointGain.getName());
        assertEquals("PGDesc", testPointGain.getDescription());
        assertEquals(60, testPointGain.getPoints());
    }
}
