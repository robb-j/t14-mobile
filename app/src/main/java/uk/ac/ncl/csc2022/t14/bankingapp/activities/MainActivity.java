package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.AwardsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.ATMFinder.ATMFinderFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking.BankingFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.budgeting.BudgetingFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.rewards.RewardsFragment;


/**
 *
 */
public class MainActivity extends LloydsActionBarActivity implements BudgetingFragment.OnFragmentInteractionListener, RewardsFragment.OnFragmentInteractionListener, ATMFinderFragment.OnFragmentInteractionListener{

    // number of tabs
    private static int tabs = 4;

    // Method from fragment interfaces.
    public static FragmentManager fragmentManager;
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

        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        // logs the user out
        findViewById(R.id.btn_logout).callOnClick();
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
                case 2:
                    // Rewards fragment activity
                    return RewardsFragment.newInstance();
                case 3:
                    // ATMFinder fragment activity
                    return ATMFinderFragment.newInstance();

                /* add new tabs here. Change the amount of tabs with the tabs variable. */
            }

            return null;
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

                case 2:
                    return "Rewards";

                case 3:
                    return "Tools";

                // ...
            }
            return "Error";
        }
    }
}
