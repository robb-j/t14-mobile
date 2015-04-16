package com.team14Tests.tests;

/**
 * Template Class created by Jack on 17/02/2015.
 * Test Class created by Aidan on 01/04/2015
 */

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ProductTest {

    //Setup global test variables
    private Product testProduct;
    private Parcel testParcel;

    @Before
    public void setup() {
        testProduct = new Product(8, "testProd", "testContent");
    }

    //Test Product Constructor and related get/set methods
    @Test
    public void testCanConstructNewProductWithCorrectValues() {
        //Constructor used in setup()
        assertEquals(8, testProduct.getId());
        assertEquals("testProd", testProduct.getTitle());
        assertEquals("testContent", testProduct.getContent());
    }

    //Test Describe Contents
    @Test
    public void testDescribeContentsReturnsZero() {
        assertEquals(0, testProduct.describeContents());
    }
}
