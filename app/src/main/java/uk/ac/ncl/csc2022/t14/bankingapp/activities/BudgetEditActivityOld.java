package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BudgetEditAdapterOld;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class BudgetEditActivityOld extends LloydsActionBarActivity implements BudgetUpdateDelegate {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit_old);
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
    public void updateBudgetPassed() {

    }

    @Override
    public void updateBudgetFailed(String errMessage) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private BudgetEditAdapterOld adapter = null;
        RecyclerView recyclerView;
        ArrayList<BudgetGroup> tempGroups = new ArrayList<>();
        ArrayList<BudgetGroup> originalGroups = new ArrayList<>();


        public PlaceholderFragment() {
        }

        @Override
        public void onResume() {
            super.onResume();



            for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
                BudgetGroup tempGroup = new BudgetGroup(group.getId(), group.getName());
                BudgetGroup originalGroup = new BudgetGroup(group.getId(), group.getName());
                for (BudgetCategory category : group.getCategories()) {
                    tempGroup.getCategories().add(category);
                    originalGroup.getCategories().add(category);
                }

                originalGroups.add(originalGroup);
                tempGroups.add(tempGroup);
            }

            adapter = new BudgetEditAdapterOld(getActivity(), tempGroups, originalGroups);

            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_budget_edit_old, container, false);

            recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_edit_budget);




            // Setting up buttons
            Button btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
            Button btnSave = (Button)rootView.findViewById(R.id.btn_save);
            TextView textNewGroup = (TextView)rootView.findViewById(R.id.text_new_group);

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

            textNewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.addItem(new BudgetGroup(BudgetGroup.TYPE_NEW, "New Group"));
                }
            });


            return rootView;
        }

        /**
         * Sends the changes made to the server.
         */
        public void saveBudget() {

            ServerInterface budgetUpdater = new DummyServerConnector();
            budgetUpdater.updateBudget(adapter.getAllGroups(), (BudgetUpdateDelegate)getActivity());
        }
    }
}
