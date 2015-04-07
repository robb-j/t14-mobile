package com.jackissogreat.tests;

/**
 * A Class to test JSONFetcher 's testing capabilities
 * Created by Rob A on 05/04/15.
 */
import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.jackissogreat.RobolectricGradleTestRunner;

import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.JSONFetcher;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.JSONTaskDelegate;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest=Config.NONE)
public class JSONFetcherTest extends InstrumentationTestCase implements JSONTaskDelegate {

    private JSONFetcher testFetcher;

    private boolean fetchSuccess;
    private String fetchMessage;
    private JSONObject fetchedObject;

    @Before
    public void setup() throws Exception {
        testFetcher = new JSONFetcher(JSONFetcher.TEST_MODE_BASEURL);
        super.setUp();
    }


    @Override
    public void taskCompleted(boolean success, String message, JSONObject json) {

        fetchSuccess = success;
        fetchMessage = message;
        fetchedObject = json;
    }


    @Test
    public void testFetcherConstruction() {
        assertNotNull(testFetcher);
    }

    @Test
    public void testFetcherTestingMode() {
        assertTrue(testFetcher.isTesting());
    }

    @Test
    public void testGettingJSONFile() {

        testFetcher.performFetch("test", null, this);


        // Test the callback
        assertTrue(fetchSuccess);
        assertNotNull(fetchedObject);
        assertNotNull(fetchMessage);


        // Test the actual json object
        try {
            assertEquals(fetchedObject.getString("TestString"), "My String");
            assertEquals(fetchedObject.getInt("TestInt"), 7);
            assertEquals(fetchedObject.getDouble("TestDouble"), 3.14, 0.01);
            assertEquals(fetchedObject.getJSONObject("TestObject").getString("Property"), "Name");
        }
        catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testGetFiles() {

        // Work out the url
        String dir = System.getProperty("user.dir");
        String path = dir + "/roboelectric-tests/src/test/assets/test.json";
        String json = null;

        try {
            InputStream is = new FileInputStream(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            json = new String(buffer, "UTF-8");

            is.close();
        }
        catch (IOException e) {}

        assertNotNull(json);
    }

}