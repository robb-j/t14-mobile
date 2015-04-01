package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BudgetAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.EditBudgetAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;

public class BudgetEditActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_edit, menu);
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

        EditBudgetAdapter adapter = null;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_budget_edit, container, false);

            refreshBudget(rootView);

            // Setting up buttons
            /*/ Button btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
            Button btnSave = (Button)rootView.findViewById(R.id.btn_save);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // close the activity
                    getActivity().finish();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    saveBudget();

                    // close activity
                    getActivity().finish();
                }
            });*/

            return rootView;
        }

        /**
         * Sends the changes made to the server.
         */
        public void saveBudget() {

            // create a MonthBudget object (TODO: REPLACE ID)
            MonthBudget monthBudget = new MonthBudget(1);

            // put the information into a MonthBudget object (group and category name changes)


            // pass the information to the server connector
        }

        public void refreshBudget(View v) {

        /* List of budget groups and categories. */
            ListView listBudgets = (ListView)v.findViewById(R.id.list_edit_budget);

            adapter = new EditBudgetAdapter(getActivity());

            for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
                adapter.addSectionHeaderItem(group);
                for (BudgetCategory category : group.getCategories()) {
                    adapter.addItem(category);
                }
                adapter.addNewItem("+ New Category");
            }
            adapter.addNewItem("+ New Group");

            listBudgets.setAdapter(adapter);
        }
    }
}
