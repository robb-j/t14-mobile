package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Context;
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
import android.widget.TextView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;

public class LoginActivity extends ActionBarActivity implements LoginDelegate{
    int first;
    int second;
    int third;

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

        //set up the initial random characters, displayed as hints
        Random randomG = new Random();
        first = (randomG.nextInt(6)+1);
        second = (first+randomG.nextInt(8-first)+1);
        third = randomG.nextInt(2);
        TextView password1 = (TextView)findViewById(R.id.passwordchar1);
        password1.setHint(first);
        TextView password2 = (TextView)findViewById(R.id.passwordchar2);
        password2.setHint(second);
        TextView password3 = (TextView)findViewById(R.id.passwordchar3);
        password3.setHint(third);
    }

    @Override
    public void loginPassed(User user, List<Product> products, String token) {

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
    public void Send()
    {
        //calling the text views for analysis
        final TextView username = (TextView)findViewById(R.id.username);
        final TextView password1 = (TextView)findViewById(R.id.passwordchar1);
        final TextView password2 = (TextView)findViewById(R.id.passwordchar2);
        final TextView password3 = (TextView)findViewById(R.id.passwordchar3);

        //adding the password characters into a list
        List<String> password = new ArrayList<String>();
        password.add(password1.getText().toString());
        password.add(password2.getText().toString());
        password.add(password3.getText().toString());

        //adding the indicies into a list
        List<Integer> indicies = new ArrayList<Integer>();
        indicies.add(first);
        indicies.add(second);
        indicies.add(third);

        String un = username.getText().toString();
        LoginDelegate LD = new LoginDelegate()
        {
            @Override
            public void loginPassed(User user, List<Product> products, String token)
            {

            }

            @Override
            public void loginFailed(String errMessage) {
                //Set all the fields to blank
                password1.setText("");
                password2.setText("");
                password3.setText("");
                username.setText("");

                //set the errormessage to visible
                TextView errMessageDisplay = (TextView)findViewById(R.id.IncorrectPasswordMessage);
                errMessageDisplay.setVisibility(View.VISIBLE);
                errMessageDisplay.setText(errMessage);

                //set the random characters again, displayed as hints
                username.setHint("Username");
                Random randomG = new Random();
                first = (randomG.nextInt(6)+1);
                second = (first+randomG.nextInt(8-first)+1);
                third = randomG.nextInt(2);
                TextView password1 = (TextView)findViewById(R.id.passwordchar1);
                password1.setHint(first);
                TextView password2 = (TextView)findViewById(R.id.passwordchar2);
                password2.setHint(second);
                TextView password3 = (TextView)findViewById(R.id.passwordchar3);
                password3.setHint(third);



            }
        };

        DummyServerConnector DSC = new DummyServerConnector();
        DSC.login(un, password, indicies, LD); //what now?

    }

    public void focuskeyboard(EditText textview)
    {


    }
    public void randomcharGenerator() //generates the character numbers
    {

        Random randomG = new Random();
        first = (randomG.nextInt(6)+1);
        second = (first+randomG.nextInt(8-first)+1);
        third = randomG.nextInt(2);
        TextView password1 = (TextView)findViewById(R.id.passwordchar1);
        password1.setHint(first);
        TextView password2 = (TextView)findViewById(R.id.passwordchar2);
        password2.setHint(second);
        TextView password3 = (TextView)findViewById(R.id.passwordchar3);
        password3.setHint(third);
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
}
