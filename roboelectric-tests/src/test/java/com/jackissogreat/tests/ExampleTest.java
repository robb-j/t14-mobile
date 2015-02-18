package com.jackissogreat.tests;

/**
 * Created by Jack on 16/02/2015.
 */
import uk.ac.ncl.csc2022.t14.bankingapp.models.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ExampleTest {

    User u = new User(1729,"Jack","Cooper","9/5/95");

    @Before
    public void instantiate() {
        u.setId(28);
    }

    @Test
    public void pleaseWork() {
        assertEquals(u.getId(),28);
    }
}
