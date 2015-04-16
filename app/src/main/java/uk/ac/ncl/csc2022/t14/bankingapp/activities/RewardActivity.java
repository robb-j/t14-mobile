package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class RewardActivity extends LloydsActionBarActivity implements ChooseRewardDelegate {

    private static int rewardID;
    private static Reward reward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // Get the reward ID from the previous activity
        rewardID = getIntent().getExtras().getInt("rewardID");
        List<Reward> rewardsList = DataStore.sharedInstance().getRewards();

        // Set the reward to be the one which was selected previously
        for (Reward r : rewardsList) {
            if (rewardID == r.getId()) {
                reward = r;
                break;
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reward, menu);
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
    public void chooseRewardPassed() {

        // Activity finishes upon successful reward retrieval

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reward, container, false);

            // Get the title and content from fragment_product
            TextView title = (TextView) rootView.findViewById(R.id.textView_rewardTitle);
            TextView content = (TextView) rootView.findViewById(R.id.textView_rewardContent);

            // Set the title and content of this product to appear on the fragment
            title.setText(reward.getName());
            content.setText(android.text.Html.fromHtml("<p>"+reward.getDescription() + "</p>"));

            // Get the selection button from the fragment
            Button selectionButton = (Button) rootView.findViewById(R.id.btn_chooseReward);
            selectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    class confirmRewardDialogFragment extends DialogFragment {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("Choose this Reward?");
                            builder.setMessage("This cannot be undone")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //If yes was selected then run the chooseReward method from the server connector
                                            ServerInterface sbi = DataStore.sharedInstance().getConnector();
                                            sbi.chooseReward(reward, (ChooseRewardDelegate) getActivity());
                                            getActivity().finish();
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
            });

            return rootView;


        }
    }
}
