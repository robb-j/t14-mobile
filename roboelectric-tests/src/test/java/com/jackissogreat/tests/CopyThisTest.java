package com.jackissogreat.tests;

/**
 * Created by Jack on 17/02/2015.
 */
import uk.ac.ncl.csc2022.t14.bankingapp.*;
import com.jackissogreat.RobolectricGradleTestRunner;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

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
