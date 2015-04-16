package com.team14Tests.tests;

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

    User mTestUser;

    @Before
    public void setup() {

        mTestUser = new User(1729,"jackissogreat", "Jack","Cooper","9/5/95");
    }

    @Test
    public void canCreateAUser() {

        assertNotNull("We should be able to create a user", mTestUser);
    }
}
