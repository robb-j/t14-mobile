package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
        MonthBudget newBudget = new MonthBudget(1);

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_budget_edit, container, false);

            refreshBudget(rootView);

            // Setting up buttons
            Button btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
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
            });



            return rootView;
        }

        /**
         * Sends the changes made to the server.
         */
        public void saveBudget() {

            // create a MonthBudget object (TODO: REPLACE ID)
            MonthBudget monthBudget = DataStore.sharedInstance().getCurrentUser().getCurrentBudget();
            // monthBudget.setNewGroups(adapter.getGroups());
            DataStore.sharedInstance().getCurrentUser().setCurrentBudget(monthBudget);



            // put the information into a MonthBudget object (group and category name changes)


            // pass the information to the server connector
        }

        public void refreshBudget(View v) {

        /* List of budget groups and categories. */
            final ListView listBudgets = (ListView)v.findViewById(R.id.list_edit_budget);

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
            listBudgets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (adapter.getItemViewType(position)) {
                        case EditBudgetAdapter.TYPE_SEPARATOR:

                            break;
                        case EditBudgetAdapter.TYPE_ITEM:

                            break;
                        case EditBudgetAdapter.TYPE_NEW:
                            // if new group
                            if (position == adapter.getCount()-1) {
                                addNewGroup(position);
                            } else {
                                addNewCategory(position);
                            }

                            break;
                    }

                }
            });
            listBudgets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    switch (adapter.getItemViewType(position)) {
                        case EditBudgetAdapter.TYPE_SEPARATOR:
                            AlertDialog.Builder builderSep = new AlertDialog.Builder(getActivity())
                                    .setMessage("Are you sure you want to delete the group: " + ((BudgetGroup) adapter.getItem(position)).getName() + "?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            adapter.removeItem(position);
                                        }
                                    })
                                    .setNegativeButton("No", null);

                            AlertDialog alertSep = builderSep.create(); // create one

                            alertSep.show(); //display it
                            break;
                        case EditBudgetAdapter.TYPE_ITEM:
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(getActivity())
                                    .setMessage("Are you sure you want to delete the category: " + ((BudgetCategory) adapter.getItem(position)).getName() + "?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            adapter.removeItem(position);
                                        }
                                    })
                                    .setNegativeButton("No", null);

                            AlertDialog alertItem = builderItem.create(); // create one

                            alertItem.show(); //display it
                            break;
                    }

                    return false;
                }
            });
        }

        public void addNewGroup(int pos) {
            adapter.addHeaderAtPos(new BudgetGroup(0, "New Group"), pos);
            adapter.addNewAtPos("+ New Category", pos + 1);
        }

        public void addNewCategory(int pos) {
            adapter.addItemAtPos(new BudgetCategory(0, "New Category", 0, 0), pos);
        }
    }
}
