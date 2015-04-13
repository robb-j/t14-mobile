package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class RewardsActivity extends LloydsActionBarActivity implements ChooseRewardDelegate{


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

    @Override
    public void onResume() {
        super.onResume();
        //Update the user's current points
        TextView currentPoints = (TextView) findViewById(R.id.textView_currentPoints);
        currentPoints.setText("Current Points: " + DataStore.sharedInstance().getCurrentUser().getPoints());
    }

    @Override
    public void chooseRewardPassed() {
        // Display a message at the top of the screen telling a user their reward selection was successful
        TextView passMsg = (TextView) findViewById(R.id.textView_selectionResponse);
        passMsg.setTextColor(Color.parseColor("#22B14C")); // Default green was pretty obnoxious, this is green but not blinding
        passMsg.setText("Reward successfully acquired");
        //Update user's current points
        TextView currentPoints = (TextView) findViewById(R.id.textView_currentPoints);
        currentPoints.setText("Current Points: " + DataStore.sharedInstance().getCurrentUser().getPoints());
    }

    @Override
    public void chooseRewardFailed(String errMessage) {
        // Display the error message at the top of the screen so a user knows what went wrong
        TextView failMsg = (TextView) findViewById(R.id.textView_selectionResponse);
        failMsg.setTextColor(Color.parseColor("#ED1C24")); // Again, red but not too red
        failMsg.setText("Error: " + errMessage);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, final int position, long id) {

                class confirmRewardDialogFragment extends DialogFragment {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Set the response message to be blank, so if a user gets the same message twice in a row it's still obvious something is happening underneath
                        TextView passMsg = (TextView) getActivity().findViewById(R.id.textView_selectionResponse);
                        passMsg.setText("");

                        builder.setTitle("Choose this Reward?");
                        builder.setMessage("This cannot be undone")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //If yes was selected then run the chooseReward method from the server connector
                                        ServerInterface sbi = DataStore.sharedInstance().getConnector();
                                        sbi.chooseReward(DataStore.sharedInstance().getRewards().get(position), (ChooseRewardDelegate) getActivity());

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //If cancel was selected then dismiss the dialog and do nothing
                                        dialog.dismiss();
                                    }
                                });
                        // Create the AlertDialog object and return it
                        return builder.create();
                    }
                }

                //Display the above dialog
                DialogFragment confirmReward = new confirmRewardDialogFragment();
                confirmReward.show(getActivity().getFragmentManager(),"");
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

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

            // Set up the spin button to take the user to the spinner view
            Button btnGoToSpin = (Button) rootView.findViewById(R.id.btn_goto_spin);

            btnGoToSpin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSpin(v);
                }
            });

        return rootView;
        }

        public void goToSpin(View v) {
            //Start a spin activity
            Intent i = new Intent(getActivity(), SpinActivity.class);
            startActivity(i);
        }

    }
}
