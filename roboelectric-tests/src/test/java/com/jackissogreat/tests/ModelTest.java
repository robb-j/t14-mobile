package com.jackissogreat.tests;

/**
 * Created by Jack on 17/02/2015.
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import uk.ac.ncl.csc2022.t14.bankingapp.models.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ModelTest {

    User u;
    Account a;
    Product p1;
    Product p2;
    Product p3;
    Transaction t;

    @Before
    public void setup() {
        u = new User(1,"First", "Last", "01/01/1970");
        p1 = new Product(10, "Product 1", "P1 Desc");
        p2 = new Product(20, "Product 2", "P2 Desc");
        p3 = new Product(30, "Product 3", "P3 Desc");
        a = new Account(2,"Student Account",500, new Product[]{p1,p2,p3});
        t = new Transaction(3, 300, a, "First");
    }

    @Test
    public void canCreateModels() {
        assertNotNull(u);
        assertNotNull(a);
        assertNotNull(p1);
        assertNotNull(t);
    }

    @Test
    public void canAssignValuesToUser() {
        assertEquals(1, u.getId());
        assertEquals("First", u.getFirstName());
        assertEquals("Last", u.getLastName());
        assertEquals("01/01/1970", u.getDob());
        assertEquals("01/01/1970", u.getLastFullCategorise());

        //Need to check if accounts are added properly to a user's account list when this is implemented
    }

    @Test
    public void canAssignValuesToAccount() {
        a.setBalance(1000);
        assertEquals(2, a.getId());
        assertEquals("Student Account", a.getName());
        assertEquals(1000, a.getBalance(), 0.1);
        assertEquals(500, a.getOverdraftLimit());
        assertArrayEquals(new Product[]{p1, p2, p3}, a.getProducts());

        // Still need to test transactions list is added to appropriately once it's implemented
    }

    @Test
    public void canAssignValuesToProduct() {
        assertEquals(10, p1.getId());
        assertEquals("Product 1", p1.getTitle());
        assertEquals("P1 Desc", p1.getContent());
    }

    @Test
    public void canAssignValuesToTransaction() {
        assertEquals(3, t.getId());
        assertEquals(300, t.getAmount(), 0.1);
        assertEquals(a, t.getAccount());
        assertEquals("First", t.getPayee());
    }
}
