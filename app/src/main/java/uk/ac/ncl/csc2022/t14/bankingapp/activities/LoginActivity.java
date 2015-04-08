package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;

public class LoginActivity extends ActionBarActivity implements LoginDelegate{

    private static char[] password = new char[3];
    private static int[] indices = new int[3];
    private static Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // set the default server connector
        DataStore.sharedInstance().setServerConnector(new DummyServerConnector());

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(this).inflate(R.layout.fragment_action_bar, null);
        getSupportActionBar().setCustomView(customNav, lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // make switch connector button visible
        findViewById(R.id.btn_switch_connector).setVisibility(View.VISIBLE);

        // make logout invisible
        findViewById(R.id.btn_logout).setVisibility(View.INVISIBLE);

    }

    @Override
    public void loginPassed(User user) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void loginFailed(String errMessage) {

        // reset activity
        findViewById(R.id.btn_logout).callOnClick();

        // produce error message.
        Toast.makeText(this, errMessage, Toast.LENGTH_LONG).show();

    }

    public void resetFields() {
        final TextView username = (TextView)findViewById(R.id.edit_username);
        final TextView password1 = (TextView)findViewById(R.id.password_char_1);
        final TextView password2 = (TextView)findViewById(R.id.password_char_2);
        final TextView password3 = (TextView)findViewById(R.id.password_char_3);
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

    public void btnSwitchConnector(View v) {
        if (DataStore.sharedInstance().getConnector() instanceof DummyServerConnector) {
            DataStore.sharedInstance().setServerConnector(new LiveServerConnector());
            Toast.makeText(LoginActivity.this, "Connector switched to Live", Toast.LENGTH_SHORT).show();
        } else {
            DataStore.sharedInstance().setServerConnector(new DummyServerConnector());
            Toast.makeText(LoginActivity.this, "Connector switched to Dummy", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);

            final EditText[] passwordChars = new EditText[3];

            passwordChars[0] = (EditText) rootView.findViewById(R.id.password_char_1);
            passwordChars[1] = (EditText) rootView.findViewById(R.id.password_char_2);
            passwordChars[2] = (EditText) rootView.findViewById(R.id.password_char_3);

            final Button btnLogin = (Button) rootView.findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(this);
            passwordChars[0].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (passwordChars[0].length() > 0)
                        passwordChars[1].requestFocus();
                }
            });
            passwordChars[1].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (passwordChars[1].length() > 0)
                        passwordChars[2].requestFocus();
                }
            });
            passwordChars[2].setOnEditorActionListener(new EditText.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                    if (arg1 == EditorInfo.IME_ACTION_DONE) {
                        btnLogin.performClick();
                    }
                    return false;
                }

            });

            generateRandomIndices();

            passwordChars[0].setHint(addSuffixToNumber(indices[0]));
            passwordChars[1].setHint(addSuffixToNumber(indices[1]));
            passwordChars[2].setHint(addSuffixToNumber(indices[2]));

            TextView username = (TextView)rootView.findViewById(R.id.edit_username);
            TextViewFocus(username);






            return rootView;
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



        }

        /**
         * Returns a string which is a number with an appropriate added suffix.
         * @param i Integer to be used to return the string with a suffix added. 6 goes to 2nd last and 7 goes to last.
         * @return
         */
        public String addSuffixToNumber(int i) {
            switch(i%10) {
                case 0: return (i+1) + "st";
                case 1: return (i+1) + "nd";
                case 2: return (i+1) + "rd";
                case 6: return "2nd last";
                case 7: return "last";
                default: return (i+1) + "th";
            }
        }


            public void TextViewFocus(TextView TV)
            {
                TV.requestFocus();

                this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // login button
                case R.id.btn_login:
                    // calling the text views for analysis
                    final TextView usernameField = (TextView)getView().findViewById(R.id.edit_username);
                    final TextView password1 = (TextView)getView().findViewById(R.id.password_char_1);
                    final TextView password2 = (TextView)getView().findViewById(R.id.password_char_2);
                    final TextView password3 = (TextView)getView().findViewById(R.id.password_char_3);
                    String username = null;


                    try {
                        username = usernameField.getText().toString();
                        password[0] = password1.getText().charAt(0);
                        password[1] = password2.getText().charAt(0);
                        password[2] = password3.getText().charAt(0);
                    } catch (NullPointerException e) {
                        ((LoginActivity)getActivity()).loginFailed("Field(s) left blank");
                        return;
                    }  catch (IndexOutOfBoundsException e) {
                        ((LoginActivity)getActivity()).loginFailed("Field(s) left blank");
                        return;
                    }





                    // create the dummy server connector
                    ServerInterface dummy = DataStore.sharedInstance().getConnector();

                    // call the login method
                    dummy.login(username, password, indices, (LoginActivity)getActivity());
                    break;
            }
        }
    }




}
