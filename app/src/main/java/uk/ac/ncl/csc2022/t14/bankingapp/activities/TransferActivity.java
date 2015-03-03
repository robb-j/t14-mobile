package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;

public class TransferActivity extends ActionBarActivity implements TransferDelegate {

    private static Account accountFrom;
    private static User user;
    private static Spinner spinner;
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Receives the accountFrom and user that were passed through to this activity
        accountFrom = getIntent().getExtras().getParcelable("account");
        user = getIntent().getExtras().getParcelable("user");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
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
    public void transferPassed(Account accFrom, Account accTo, double amount) {

    }

    @Override
    public void transferFailed(String errMessage) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private double amount;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);

            // Set the outgoing accountFrom to be the accountFrom we clicked "Make a Transfer" from
            TextView outgoingAccount = (TextView) rootView.findViewById(R.id.outgoingAccount);
            outgoingAccount.setText(accountFrom.getName());

            // Set the list of accounts to pay into to be a choice of all of the user's accounts, excluding
            // the one which is already being paid from.
            List<Account> accounts = user.getAccounts();
            List<String> accountNames = new ArrayList<>();

            for (int i = 0; i < accounts.size(); i++)
            { // For each accountFrom the user owns
                if ( !(accounts.get(i).getName().equals(accountFrom.getName())) )
                { // Add the accountFrom to the list of options on the spinner
                  // unless it is the accountFrom the transfer is being made from
                    accountNames.add(accounts.get(i).getName());
                }
            }

            // Use the adapter to give these values to the spinner
            spinner = (Spinner) rootView.findViewById(R.id.spinner_accounts);
            adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, accountNames);
            spinner.setAdapter(adapter);

            final TextView transferAmount = (TextView) rootView.findViewById(R.id.transferAmount);

            // Setting up the "make a transfer" button. See btnMakeTransfer(View v)
            Button btn = (Button) rootView.findViewById(R.id.btn_make_transfer);

            // Calls btnMakeTransfer(View v) when clicked.
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set the accountFrom to be transferred to
                    String accName = (String) spinner.getSelectedItem();
                    List<Account> accounts = user.getAccounts();
                    Account accountTo = new Account(0,null,0,0,null);

                    for (int i = 0; i < accounts.size(); i++ )
                    { // For each accountFrom the user owns
                        if (accounts.get(i).getName().equals(accName))
                        { //If the name matches the accountFrom name in the spinner select this accountFrom
                            accountTo = accounts.get(i);
                            break;
                        }
                    }

                    //Set amount to be the current value in the amount field
                    amount = Double.parseDouble(transferAmount.getText().toString());

                    btnMakeTransfer(v, accountTo, amount);
                }
            });

            return rootView;
        }

        public void btnMakeTransfer(View v, Account accountTo, double amount) {
            ServerInterface transferrer = new DummyServerConnector();
            transferrer.makeTransfer(accountFrom,accountTo, amount, "correctToken", (TransferDelegate) this.getActivity());
        }
    }
}
