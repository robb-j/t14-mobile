package com.team14Tests.tests;

/**
 * Created by Jack on 17/02/2015.
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CopyThisTest {

    @Before
    public void setup() {
        //Setup variables here
    }

    @Test
    public void shouldFail() {
        fail("This needs to be deleted.");
    }
}
