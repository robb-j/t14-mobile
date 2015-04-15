package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.fragments.EditCategoryFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.fragments.EditGroupFragment;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class EditBudgetActivity extends LloydsActionBarActivity implements BudgetUpdateDelegate, EditGroupFragment.OnFragmentInteractionListener, EditCategoryFragment.OnFragmentInteractionListener {

    FragmentManager manager;
    public List<BudgetGroup> groups = new ArrayList<>();

    public List<BudgetGroup> getGroups() {
        return groups;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        manager = getFragmentManager();

        Fragment fragment = new EditGroupFragment();
        // insert the fragment replacing the existing fragment

        manager.beginTransaction().replace(R.id.content_frame_edit_budget, fragment)
                .commit();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_budget, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateBudgetPassed() {
        Toast.makeText(this, "Updated Budget", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBudgetFailed(String errMessage) {
        Toast.makeText(this, "Error: " + errMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

    }

    public void loadEditGroup(int position) {
        Fragment fragment = new EditCategoryFragment();
        // insert the fragment replacing the existing fragment
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        manager.beginTransaction().replace(R.id.content_frame_edit_budget, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        groups = new ArrayList<>();

        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
            BudgetGroup tempGroup = new BudgetGroup(group.getId(), group.getName());
            for (BudgetCategory category : group.getCategories()) {
                tempGroup.getCategories().add(category);
            }
            groups.add(tempGroup);
        }
    }

    public void saveGroup() {
        Fragment fragment = new EditGroupFragment();
        // insert the fragment replacing the existing fragment
        manager.beginTransaction().replace(R.id.content_frame_edit_budget, fragment)
                .commit();

    }
}
