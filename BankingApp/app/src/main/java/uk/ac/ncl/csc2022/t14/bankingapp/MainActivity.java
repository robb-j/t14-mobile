package uk.ac.ncl.csc2022.t14.bankingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.ncl.csc2022.t14.bankingapp.R;


/**
 *
 */
public class MainActivity extends ActionBarActivity {


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
            return BankingFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
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
