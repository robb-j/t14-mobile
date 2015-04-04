package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.graphics.Color;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerBudgetingInterface;

public class SpinActivity extends ActionBarActivity implements PointSpinDelegate {

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
    public void spinPassed(int numPoints) {

        // Retrieve the spin wheel and set an animation for it
        ImageView animTarget = (ImageView) findViewById(R.id.imageView_spinWheel);
        Animation animation = null;

        // Display the correct animation based on the points received
        switch (numPoints) {
            case 10:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin1);
                break;

            case 20:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin2);
                break;

            case 30:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin3);
                break;

            case 40:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin4);
                break;

            case 50:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin5);
                break;

            case 60:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin6);
                break;

            case 70:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin7);
                break;

            case 80:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin8);
                break;

            case 90:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin9);
                break;

            case 100:
                animation = AnimationUtils.loadAnimation(this, R.anim.wheel_spin10);
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

            Button btn = (Button) rootView.findViewById(R.id.btn_spin);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Perform a spin
                    ServerBudgetingInterface sbi = new DummyServerConnector();
                    sbi.performSpin((PointSpinDelegate) getActivity());
                }
            });

            return rootView;
        }
    }
}
