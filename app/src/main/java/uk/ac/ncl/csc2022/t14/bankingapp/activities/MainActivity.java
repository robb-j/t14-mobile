package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking.BankingFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.budgeting.BudgetingFragment;


/**
 *
 */
public class MainActivity extends ActionBarActivity implements BudgetingFragment.OnFragmentInteractionListener{

    private static int tabs = 2;

    // Method from fragment interfaces.
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    // Instance Variables
    SectionPagerAdapter mPagerAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the tabbing
        mPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);


        /* Test products */
        Product product = new Product(1, "New ISA offer", "lorem impsum blah blah blah yolo");


        /* Test accounts */
        Account account1 = new Account(28, "Student Account", 523.33, 500, null);
        Account account2 = new Account(1729, "Bills Account", -120.18, 1000, product);
        Account account3 = new Account(8191, "Savings Account", 23112.41, 1000, null);


        /* Test user */
        User testUser = new User(10, "Bobby99", "Joe", "Bloggs", "10/03/2015");
        testUser.getAccounts().add(account1);
        testUser.getAccounts().add(account2);
        testUser.getAccounts().add(account3);

        // Create the Data Store
        DataStore.sharedInstance().setCurrentUser(testUser);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * An Adapter to create the different tab fragments
     */
    public static class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // Banking fragment activity
                    return BankingFragment.newInstance();
                case 1:
                    // Budgeting fragment activity
                    return BudgetingFragment.newInstance();

                /* add new tabs here. Change the amount of tabs with the tabs variable. */
            }

            return null;
            // return BankingFragment.newInstance(new User(10, "Joe", "Bloggs", "10/03/2015"));
        }

        @Override
        public int getCount() {
            return tabs;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Banking";

                case 1:
                    return "Budgeting";

                // ...
            }
            return "Error";
        }
    }
}
