package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.ExpandableListAdapter;

public class CategorizeActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorize);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        ExpandableListAdapter eListAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_categorize, container, false);
            //get the list view


            getListData();

            eListAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
            getActivity().setContentView(R.layout.fragment_categorize);
            expListView = (ExpandableListView) getActivity().findViewById(R.id.list);


            expListView.setAdapter(eListAdapter);



            return rootView;
        }
        private void getListData()
        {
            //Hardcoded until I know where the transactions are coming from

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

            //adding the transactions
            listDataHeader.add("Maccy Ds");
            listDataHeader.add("Netflix");
            listDataHeader.add("Tesco");

            //adding the possible categories
            List<String> categories = new ArrayList<String>();
            categories.add("Bills");
            categories.add("Food & Drink");
            categories.add("Going out");

            listDataChild.put(listDataHeader.get(0), categories);
            listDataChild.put(listDataHeader.get(1), categories);
            listDataChild.put(listDataHeader.get(2), categories);
        }
    }


}
