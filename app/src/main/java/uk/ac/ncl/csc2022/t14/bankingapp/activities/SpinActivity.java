package uk.ac.ncl.csc2022.t14.bankingapp.activities;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class SpinActivity extends LloydsActionBarActivity implements PointSpinDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spin, menu);
        return true;
    }


    @Override
    public void spinPassed(int numPoints) {


        // Update amount of spins remaining on the spin button
        Button btn = (Button) findViewById(R.id.btn_spin);
        btn.setText("Spin! (" + DataStore.sharedInstance().getCurrentUser().getNumberOfSpins() + ")");

        // Retrieve the spin wheel and set an animation for it
        ImageView animTarget = (ImageView) findViewById(R.id.imageView_spinWheel);
        Animation animation = null;

        // Display the correct animation based on the points received
        switch (numPoints) {
            case 10:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin1);
                break;

            case 20:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin4);
                break;

            case 30:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin7);
                break;

            case 40:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin10);
                break;

            case 50:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin3);
                break;

            case 60:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin6);
                break;

            case 70:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin9);
                break;

            case 80:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin2);
                break;

            case 90:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin5);
                break;

            case 100:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin8);
                break;
        }

        animation.setFillAfter(true); // This stops the image reverting back to its original state once the animation completes
        animTarget.startAnimation(animation); // Begin the spin animation
    }

    @Override
    public void spinFailed(String errMessage) {

        TextView failMsg = (TextView) findViewById(R.id.textView_spinErrMessage);
        failMsg.setTextColor(Color.parseColor("#ED1C24")); // Red but not blindingly so
        failMsg.setText(errMessage);
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
            final View rootView = inflater.inflate(R.layout.fragment_spin, container, false);

            // Display amount of spins remaining on the spin button
            Button btn = (Button) rootView.findViewById(R.id.btn_spin);
            btn.setText("Spin! (" + DataStore.sharedInstance().getCurrentUser().getNumberOfSpins() + ")");

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Fail if the user has no spins
                    if (DataStore.sharedInstance().getCurrentUser().getNumberOfSpins() < 1) {
                        TextView failMsg = (TextView) rootView.findViewById(R.id.textView_spinErrMessage);
                        failMsg.setTextColor(Color.parseColor("#ED1C24")); // Red but not blindingly so
                        failMsg.setText("No spins remaining");
                    }
                    else {
                        // Else perform a spin
                        ServerInterface sbi = DataStore.sharedInstance().getConnector();
                        sbi.performSpin((PointSpinDelegate) getActivity());
                    }
                }
            });

            return rootView;
        }
    }
}
