package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class RewardsListActivity extends LloydsActionBarActivity {


    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rewards, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Update the user's current points
        TextView currentPoints = (TextView) findViewById(R.id.textView_currentPoints);
        currentPoints.setText("Current Points: " + DataStore.sharedInstance().getCurrentUser().getPoints());
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, final int position, long id) {

                Intent i = new Intent(getActivity(), RewardActivity.class);
                // pass through the relevant account
                Reward reward = DataStore.sharedInstance().getRewards().get(position);
                i.putExtra("rewardID", reward.getId());
                startActivity(i);
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rewards_list, container, false);

            // Get the rewards view from the fragment and the rewards from the data store
            ListView rewardsView = (ListView) rootView.findViewById(R.id.listView_rewards);
            List<Reward> rewards = DataStore.sharedInstance().getRewards();

            // Add all of the names of the rewards to a new list for a sensible display
            List<String> names = new ArrayList<>();
            for (Reward r : rewards) {
                names.add(r.getName() +" "+ r.getCost());
            }

            // Set this list of reward names to be displayed in the fragment
            adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, names);
            rewardsView.setAdapter(adapter);

            // Display the dialog above whenever an item in the list view is clicked
            rewardsView.setOnItemClickListener(mMessageClickedHandler);

            // Display the user's current points
            TextView currentPoints = (TextView) rootView.findViewById(R.id.textView_currentPoints);
            currentPoints.setText("Current Points: " + DataStore.sharedInstance().getCurrentUser().getPoints());

        return rootView;
        }

    }
}
