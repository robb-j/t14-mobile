package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;

public class LoginActivity extends ActionBarActivity implements LoginDelegate{

    char[] password = new char[3];
    int[] indices = new int[3];
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        // Hide the action bar. -----DON'T TOUCH THIS-----
        // don't have to shout
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        generateRandomIndices();
    }

    @Override
    public void loginPassed(User user) {

    }

    @Override
    public void loginFailed(String errMessage) {

    }

    public class login
   {
       // String username;
       // List<String> password = new ArrayList<String>();
       // List<Integer> indicies = new ArrayList<Integer>();
       // LoginDelegate LD = new LoginDelegate();
      // public login(String un, List<String> pw, List<Integer> indi, LoginDelegate LogD)
      // {
      //     username = un;
      //     password = pw;
      //     indicies = indi;
       //    LD = LogD;
      // }

    }
    public void Send(View view)
    {
        //calling the text views for analysis
        final TextView username = (TextView)findViewById(R.id.username);
        final TextView password1 = (TextView)findViewById(R.id.passwordchar1);
        final TextView password2 = (TextView)findViewById(R.id.passwordchar2);
        final TextView password3 = (TextView)findViewById(R.id.passwordchar3);

        //adding the password characters into a list
        char[] password = {password1.getText().charAt(0), password2.getText().charAt(0), password3.getText().charAt(0)};


        //adding the indicies into an array


        String un = username.getText().toString();
        LoginDelegate LD = new LoginDelegate()
        {
            @Override
            public void loginPassed(User user)
            {

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

            }

            @Override
            public void loginFailed(String errMessage)
            {
                //Set all the fields to blank
                password1.setText("");
                password2.setText("");
                password3.setText("");
                username.setText("");

                //set the errormessage to visible
                TextView errMessageDisplay = (TextView)findViewById(R.id.IncorrectPasswordMessage);
                errMessageDisplay.setVisibility(View.VISIBLE);
                errMessageDisplay.setText(errMessage);

                generateRandomIndices();



            }
        };

        DummyServerConnector DSC = new DummyServerConnector();
        DSC.login(un, password, indices, LD); //what now?

    }

    public void focuskeyboard(EditText textview)
    {


    }
    public void randomcharGenerator() //generates the character numbers
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public String addSuffixToNumber(int i) {
        switch(i%10) {
            case 0: return (i+1) + "st";
            case 1: return (i+1) + "nd";
            case 2: return (i+1) + "rd";
            case 6:
                EditText editText = (EditText)findViewById(R.id.passwordchar3);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editText.getLayoutParams();
                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) (80 * scale + 0.5f);
                params.width = pixels;
                return "2nd last";
            case 7: return "last";
            default: return (i+1) + "th";
        }
    }

    public void generateRandomIndices() {
        // set up the initial random characters, displayed as hints
        // first index
        int min = 0, max = 4;
        indices[0] = rand.nextInt(max - min + 1) + min;

        // second index
        min = indices[0] + 1; max = 5;
        indices[1] = rand.nextInt(max - min + 1) + min;

        // third index
        min = 6; max = 7;
        indices[2] = rand.nextInt(max - min + 1) + min;


        TextView password1 = (TextView)findViewById(R.id.passwordchar1);
        password1.setHint(addSuffixToNumber(indices[0]));
        TextView password2 = (TextView)findViewById(R.id.passwordchar2);
        password2.setHint(addSuffixToNumber(indices[1]));
        TextView password3 = (TextView)findViewById(R.id.passwordchar3);
        password3.setHint(addSuffixToNumber(indices[2]));
    }
}
